/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v3.otherCgt.retrieve

import play.api.Configuration
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.Result
import shared.config.MockSharedAppConfig
import shared.controllers.{ControllerBaseSpec, ControllerTestRunner}
import shared.models.domain.{Nino, TaxYear, Timestamp}
import shared.models.errors.{ErrorWrapper, NinoFormatError, RuleTaxYearNotSupportedError}
import shared.models.outcomes.ResponseWrapper
import shared.services.{MockEnrolmentsAuthService, MockMtdIdLookupService}
import shared.utils.MockIdGenerator
import v3.otherCgt.retrieve.def1.model.request.Def1_RetrieveOtherCgtRequestData
import v3.otherCgt.retrieve.def1.model.response.DownstreamAssetType.`otherProperty`
import v3.otherCgt.retrieve.def1.model.response.{Def1_RetrieveOtherCgtResponse, Disposal, Losses, NonStandardGains}
import v3.otherCgt.retrieve.model.request.RetrieveOtherCgtRequestData
import v3.otherCgt.retrieve.model.response.RetrieveOtherCgtResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RetrieveOtherCgtControllerSpec
    extends ControllerBaseSpec
    with ControllerTestRunner
    with MockEnrolmentsAuthService
    with MockMtdIdLookupService
    with MockRetrieveOtherCgtService
    with MockRetrieveOtherCgtValidatorFactory
    with MockIdGenerator
    with MockSharedAppConfig {

  val taxYear: String = "2019-20"

  val requestData: RetrieveOtherCgtRequestData = Def1_RetrieveOtherCgtRequestData(
    nino = Nino(validNino),
    taxYear = TaxYear.fromMtd(taxYear)
  )

  val responseModel: RetrieveOtherCgtResponse = Def1_RetrieveOtherCgtResponse(
    submittedOn = Timestamp("2021-05-07T16:18:44.403Z"),
    disposals = Some(
      List(
        Disposal(
          assetType = `otherProperty`.toMtd,
          assetDescription = "string",
          acquisitionDate = "2021-05-07",
          disposalDate = "2021-05-07",
          disposalProceeds = 59999999999.99,
          allowableCosts = 59999999999.99,
          gain = Some(59999999999.99),
          loss = None,
          claimOrElectionCodes = Some(List("OTH")),
          gainAfterRelief = Some(59999999999.99),
          lossAfterRelief = None,
          rttTaxPaid = Some(59999999999.99)
        )
      )),
    nonStandardGains = Some(
      NonStandardGains(
        carriedInterestGain = Some(19999999999.99),
        carriedInterestRttTaxPaid = Some(19999999999.99),
        attributedGains = Some(19999999999.99),
        attributedGainsRttTaxPaid = Some(19999999999.99),
        otherGains = Some(19999999999.99),
        otherGainsRttTaxPaid = Some(19999999999.99)
      )
    ),
    losses = Some(
      Losses(
        broughtForwardLossesUsedInCurrentYear = Some(29999999999.99),
        setAgainstInYearGains = Some(29999999999.99),
        setAgainstInYearGeneralIncome = Some(29999999999.99),
        setAgainstEarlierYear = Some(29999999999.99)
      )
    ),
    adjustments = Some(-39999999999.99)
  )

  val validResponseJson: JsValue = Json.parse(
    """
      |{
      |   "submittedOn":"2021-05-07T16:18:44.403Z",
      |   "disposals":[
      |      {
      |         "assetType":"other-property",
      |         "assetDescription":"string",
      |         "acquisitionDate":"2021-05-07",
      |         "disposalDate":"2021-05-07",
      |         "disposalProceeds":59999999999.99,
      |         "allowableCosts":59999999999.99,
      |         "gain":59999999999.99,
      |         "claimOrElectionCodes":[
      |            "OTH"
      |         ],
      |         "gainAfterRelief":59999999999.99,
      |         "rttTaxPaid":59999999999.99
      |      }
      |   ],
      |   "nonStandardGains":{
      |      "carriedInterestGain":19999999999.99,
      |      "carriedInterestRttTaxPaid":19999999999.99,
      |      "attributedGains":19999999999.99,
      |      "attributedGainsRttTaxPaid":19999999999.99,
      |      "otherGains":19999999999.99,
      |      "otherGainsRttTaxPaid":19999999999.99
      |   },
      |   "losses":{
      |      "broughtForwardLossesUsedInCurrentYear":29999999999.99,
      |      "setAgainstInYearGains":29999999999.99,
      |      "setAgainstInYearGeneralIncome":29999999999.99,
      |      "setAgainstEarlierYear":29999999999.99
      |   },
      |   "adjustments":-39999999999.99
      |}
     """.stripMargin
  )

  val mtdResponse: JsObject = validResponseJson.as[JsObject]

  "RetrieveOtherCgtController" should {
    "return a successful response with status 200 (OK)" when {
      "given a valid request" in new Test {
        willUseValidator(returningSuccess(requestData))

        MockRetrieveOtherCgtService
          .retrieve(requestData)
          .returns(Future.successful(Right(ResponseWrapper(correlationId, responseModel))))

        runOkTest(
          expectedStatus = OK,
          maybeExpectedResponseBody = Some(mtdResponse)
        )
      }
    }

    "return the error as per spec" when {
      "parser validation fails" in new Test {
        willUseValidator(returning(NinoFormatError))

        runErrorTest(NinoFormatError)
      }

      "the service returns an error" in new Test {
        willUseValidator(returningSuccess(requestData))

        MockRetrieveOtherCgtService
          .retrieve(requestData)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, RuleTaxYearNotSupportedError))))

        runErrorTest(RuleTaxYearNotSupportedError)
      }
    }
  }

  trait Test extends ControllerTest {

    val controller: RetrieveOtherCgtController = new RetrieveOtherCgtController(
      authService = mockEnrolmentsAuthService,
      lookupService = mockMtdIdLookupService,
      validatorFactory = mockRetrieveOtherCgtValidatorFactory,
      service = mockRetrieveOtherCgtService,
      cc = cc,
      idGenerator = mockIdGenerator
    )

    MockedSharedAppConfig.featureSwitchConfig.anyNumberOfTimes() returns Configuration(
      "supporting-agents-access-control.enabled" -> true
    )

    MockedSharedAppConfig.endpointAllowsSupportingAgents(controller.endpointName).anyNumberOfTimes() returns false

    override protected def callController(): Future[Result] = controller.retrieveOtherCgt(validNino, taxYear)(fakeGetRequest)
  }

}
