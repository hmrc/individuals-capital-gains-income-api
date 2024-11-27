/*
 * Copyright 2023 HM Revenue & Customs
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

package v1.residentialPropertyDisposals.createAmendCgtPpdOverrides

import api.connectors.ConnectorSpec
import api.mocks.MockHttpClient
import api.models.domain.{Nino, TaxYear}
import api.models.outcomes.ResponseWrapper
import config.MockAppConfig
import uk.gov.hmrc.http.HeaderCarrier
import v1.residentialPropertyDisposals.createAmendCgtPpdOverrides.def1.fixture.Def1_CreateAmendCgtPpdOverridesServiceConnectorFixture.requestBodyModel
import v1.residentialPropertyDisposals.createAmendCgtPpdOverrides.def1.model.request.Def1_CreateAmendCgtPpdOverridesRequestData

import scala.concurrent.Future

class CreateAmendCgtPpdOverridesConnectorSpec extends ConnectorSpec {

  private val nino: String = "AA111111A"

  trait Test extends MockHttpClient with MockAppConfig {

    val connector: CreateAmendCgtPpdOverridesConnector = new CreateAmendCgtPpdOverridesConnector(
      http = mockHttpClient,
      appConfig = mockAppConfig
    )

  }

  "CreateAmendCgtPpdOverridesConnector" when {
    "createAndAmend" must {
      "return a 204 status for a success scenario" in new Api1661Test with Test {

        val taxYear = TaxYear.fromMtd("2019-20")

        val request = Def1_CreateAmendCgtPpdOverridesRequestData(
          nino = Nino(nino),
          taxYear,
          body = requestBodyModel
        )

        val outcome = Right(ResponseWrapper(correlationId, ()))

        willPut(
          url = s"$baseUrl/income-tax/income/disposals/residential-property/ppd/$nino/${taxYear.asMtd}",
          body = requestBodyModel
        )
          .returns(Future.successful(outcome))

        await(connector.createAmend(request)) shouldBe outcome
      }
    }

    "createAndAmend called for a Tax Year Specific tax year" must {
      "return a 200 status for a success scenario" in
        new TysIfsTest with Test {
          def taxYear: TaxYear = TaxYear.fromMtd("2023-24")

          override implicit val hc: HeaderCarrier = HeaderCarrier(otherHeaders = otherHeaders ++ Seq("Content-Type" -> "application/json"))

          val outcome = Right(ResponseWrapper(correlationId, ()))

          val request = Def1_CreateAmendCgtPpdOverridesRequestData(
            nino = Nino(nino),
            taxYear,
            body = requestBodyModel
          )

          willPut(
            s"$baseUrl/income-tax/income/disposals/residential-property/ppd/${taxYear.asTysDownstream}/${nino}",
            requestBodyModel) returns Future
            .successful(outcome)

          val result = await(connector.createAmend(request))
          result shouldBe outcome
        }
    }

  }

}
