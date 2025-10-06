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
import v3.otherCgt.retrieve.def2.model.response.*
import v3.otherCgt.retrieve.def2.model.response.AssetType.`other-property`
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

  val def1_responseModel: RetrieveOtherCgtResponse = Def1_RetrieveOtherCgtResponse(
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

  val def2_responseModel: RetrieveOtherCgtResponse = Def2_RetrieveOtherCgtResponse(
    submittedOn = Timestamp("2026-02-07T16:18:44.403Z"),
    cryptoassets = Some(
      Seq(
        Cryptoassets(
          numberOfDisposals = 1,
          assetDescription = "description string",
          tokenName = "Name of token",
          acquisitionDate = "2025-08-04",
          disposalDate = "2025-09-04",
          disposalProceeds = 99999999999.99,
          allowableCosts = 99999999999.99,
          gainsWithBadr = Some(99999999999.99),
          gainsBeforeLosses = 99999999999.99,
          losses = Some(99999999999.99),
          claimOrElectionCodes = Some(Seq("GHO")),
          amountOfNetGain = Some(99999999999.99),
          amountOfNetLoss = Some(99999999999.99),
          rttTaxPaid = Some(99999999999.99)
        )
      )),
    otherGains = Some(
      Seq(
        OtherGains(
          assetType = `other-property`,
          numberOfDisposals = 1,
          assetDescription = "example of this asset",
          companyName = Some("Bob the Builder"),
          companyRegistrationNumber = Some("11111111"),
          acquisitionDate = "2025-04-07",
          disposalDate = "2025-07-10",
          disposalProceeds = 99999999999.99,
          allowableCosts = 99999999999.99,
          gainsWithBadr = Some(99999999999.99),
          gainsWithInv = Some(99999999999.99),
          gainsBeforeLosses = 99999999999.99,
          losses = Some(99999999999.99),
          claimOrElectionCodes = Some(Seq("GHO")),
          amountOfNetGain = Some(99999999999.99),
          amountOfNetLoss = Some(99999999999.99),
          rttTaxPaid = Some(99999999999.99)
        )
      )),
    unlistedShares = Some(
      Seq(
        UnlistedShares(
          numberOfDisposals = 1,
          assetDescription = "My asset",
          companyName = "Bob the Builder",
          companyRegistrationNumber = Some("11111111"),
          acquisitionDate = "2025-04-10",
          disposalDate = "2025-04-12",
          disposalProceeds = 99999999999.99,
          allowableCosts = 99999999999.99,
          gainsWithBadr = Some(99999999999.99),
          gainsWithInv = Some(99999999999.99),
          gainsBeforeLosses = 99999999999.99,
          losses = Some(99999999999.99),
          claimOrElectionCodes = Some(Seq("GHO")),
          gainsReportedOnRtt = Some(99999999999.99),
          gainsExceedingLifetimeLimit = Some(99999999999.99),
          gainsUnderSeis = Some(99999999999.99),
          lossUsedAgainstGeneralIncome = Some(99999999999.99),
          eisOrSeisReliefDueCurrentYear = Some(99999999999.99),
          lossesUsedAgainstGeneralIncomePreviousYear = Some(99999999999.99),
          eisOrSeisReliefDuePreviousYear = Some(99999999999.99),
          rttTaxPaid = Some(99999999999.99)
        )
      )),
    gainExcludedIndexedSecurities = Some(
      GainExcludedIndexedSecurities(
        gainsFromExcludedSecurities = Some(99999999999.99)
      )
    ),
    qualifyingAssetHoldingCompany = Some(
      QualifyingAssetHoldingCompany(
        gainsFromQahcBeforeLosses = Some(99999999999.99),
        lossesFromQahc = Some(99999999999.99)
      )
    ),
    nonStandardGains = Some(
      def2.model.response.NonStandardGains(
        attributedGains = Some(99999999999.99),
        attributedGainsRttTaxPaid = Some(99999999999.99),
        otherGains = Some(99999999999.99),
        otherGainsRttTaxPaid = Some(99999999999.99)
      )
    ),
    losses = Some(
      def2.model.response.Losses(
        broughtForwardLossesUsedInCurrentYear = Some(99999999999.99),
        setAgainstInYearGains = Some(99999999999.99),
        setAgainstEarlierYear = Some(99999999999.99),
        lossesToCarryForward = Some(99999999999.99)
      )
    ),
    adjustments = Some(
      Adjustments(
        adjustmentAmount = Some(99999999999.99)
      )
    ),
    lifetimeAllowance = Some(
      LifetimeAllowance(
        lifetimeAllowanceBadr = Some(99999999999.99),
        lifetimeAllowanceInv = Some(99999999999.99)
      )
    )
  )

  val def1_validResponseJson: JsValue = Json.parse(
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

  val def2_validResponseJson: JsValue = Json.parse(
    """
      {
      |    "submittedOn": "2026-02-07T16:18:44.403Z",
      |    "cryptoassets": [
      |        {
      |            "numberOfDisposals": 1,
      |            "assetDescription": "description string",
      |            "tokenName": "Name of token",
      |            "acquisitionDate": "2025-08-04",
      |            "disposalDate": "2025-09-04",
      |            "disposalProceeds": 99999999999.99,
      |            "allowableCosts": 99999999999.99,
      |            "gainsWithBadr": 99999999999.99,
      |            "gainsBeforeLosses": 99999999999.99,
      |            "losses": 99999999999.99,
      |            "claimOrElectionCodes": [
      |                "GHO"
      |            ],
      |            "amountOfNetGain": 99999999999.99,
      |            "amountOfNetLoss": 99999999999.99,
      |            "rttTaxPaid": 99999999999.99
      |        }
      |    ],
      |    "otherGains": [
      |        {
      |            "assetType": "other-property",
      |            "numberOfDisposals": 1,
      |            "assetDescription": "example of this asset",
      |            "companyName": "Bob the Builder",
      |            "companyRegistrationNumber": "11111111",
      |            "acquisitionDate": "2025-04-07",
      |            "disposalDate": "2025-07-10",
      |            "disposalProceeds": 99999999999.99,
      |            "allowableCosts": 99999999999.99,
      |            "gainsWithBadr": 99999999999.99,
      |            "gainsWithInv": 99999999999.99,
      |            "gainsBeforeLosses": 99999999999.99,
      |            "losses": 99999999999.99,
      |            "claimOrElectionCodes": [
      |                "GHO"
      |            ],
      |            "amountOfNetGain": 99999999999.99,
      |            "amountOfNetLoss": 99999999999.99,
      |            "rttTaxPaid": 99999999999.99
      |        }
      |    ],
      |    "unlistedShares": [
      |        {
      |            "numberOfDisposals": 1,
      |            "assetDescription": "My asset",
      |            "companyName": "Bob the Builder",
      |            "companyRegistrationNumber": "11111111",
      |            "acquisitionDate": "2025-04-10",
      |            "disposalDate": "2025-04-12",
      |            "disposalProceeds": 99999999999.99,
      |            "allowableCosts": 99999999999.99,
      |            "gainsWithBadr": 99999999999.99,
      |            "gainsWithInv": 99999999999.99,
      |            "gainsBeforeLosses": 99999999999.99,
      |            "losses": 99999999999.99,
      |            "claimOrElectionCodes": [
      |                "GHO"
      |            ],
      |            "gainsReportedOnRtt": 99999999999.99,
      |            "gainsExceedingLifetimeLimit": 99999999999.99,
      |            "gainsUnderSeis": 99999999999.99,
      |            "lossUsedAgainstGeneralIncome": 99999999999.99,
      |            "eisOrSeisReliefDueCurrentYear": 99999999999.99,
      |            "lossesUsedAgainstGeneralIncomePreviousYear": 99999999999.99,
      |            "eisOrSeisReliefDuePreviousYear": 99999999999.99,
      |            "rttTaxPaid": 99999999999.99
      |        }
      |    ],
      |    "gainExcludedIndexedSecurities": {
      |        "gainsFromExcludedSecurities": 99999999999.99
      |    },
      |    "qualifyingAssetHoldingCompany": {
      |        "gainsFromQahcBeforeLosses": 99999999999.99,
      |        "lossesFromQahc": 99999999999.99
      |    },
      |    "nonStandardGains": {
      |        "attributedGains": 99999999999.99,
      |        "attributedGainsRttTaxPaid": 99999999999.99,
      |        "otherGains": 99999999999.99,
      |        "otherGainsRttTaxPaid": 99999999999.99
      |    },
      |    "losses": {
      |        "broughtForwardLossesUsedInCurrentYear": 99999999999.99,
      |        "setAgainstInYearGains": 99999999999.99,
      |        "setAgainstEarlierYear": 99999999999.99,
      |        "lossesToCarryForward": 99999999999.99
      |    },
      |    "adjustments": {
      |        "adjustmentAmount": 99999999999.99
      |    },
      |    "lifetimeAllowance": {
      |        "lifetimeAllowanceBadr": 99999999999.99,
      |        "lifetimeAllowanceInv": 99999999999.99
      |    }
      |}
     """.stripMargin
  )

  "RetrieveOtherCgtController" should {
    "return a successful Def1 response with status 200 (OK)" when {
      "given a valid request" in new Test {
        willUseValidator(returningSuccess(requestData))

        MockRetrieveOtherCgtService
          .retrieve(requestData)
          .returns(Future.successful(Right(ResponseWrapper(correlationId, def1_responseModel))))

        runOkTest(
          expectedStatus = OK,
          maybeExpectedResponseBody = Some(def1_validResponseJson.as[JsObject])
        )
      }
    }

    "return a successful Def2 response with status 200 (OK)" when {
      "given a valid request" in new Test {
        override val taxYear: String = "2025-26"
        willUseValidator(returningSuccess(requestData))

        MockRetrieveOtherCgtService
          .retrieve(requestData)
          .returns(Future.successful(Right(ResponseWrapper(correlationId, def2_responseModel))))

        runOkTest(
          expectedStatus = OK,
          maybeExpectedResponseBody = Some(def2_validResponseJson.as[JsObject])
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

    val taxYear = "2019-20"

    val requestData: RetrieveOtherCgtRequestData = Def1_RetrieveOtherCgtRequestData(
      nino = Nino(validNino),
      taxYear = TaxYear.fromMtd(taxYear)
    )

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
