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

package api.connectors

import api.mocks.MockHttpClient
import mocks.MockAppConfig

import scala.concurrent.Future

class MtdIdLookupConnectorSpec extends ConnectorSpec {

  class Test extends MockHttpClient with MockAppConfig {

    val connector = new MtdIdLookupConnector(
      http = mockHttpClient,
      appConfig = mockAppConfig
    )

    MockedAppConfig.mtdIdBaseUrl returns baseUrl
  }

  val nino  = "test-nino"
  val mtdId = "test-mtdId"

  "getMtdId" should {
    "return an MtdId" when {
      "the http client returns a mtd id" in new Test {
        MockedHttpClient
          .get[MtdIdLookupConnector.Outcome](s"$baseUrl/mtd-identifier-lookup/nino/$nino", config = dummyDesHeaderCarrierConfig)
          .returns(Future.successful(Right(mtdId)))

        await(connector.getMtdId(nino)) shouldBe Right(mtdId)
      }
    }

    "return an error" when {
      "the http client returns that error" in new Test {
        val statusCode: Int = IM_A_TEAPOT
        MockedHttpClient
          .get[MtdIdLookupConnector.Outcome](s"$baseUrl/mtd-identifier-lookup/nino/$nino", config = dummyDesHeaderCarrierConfig)
          .returns(Future.successful(Left(MtdIdLookupConnector.Error(statusCode))))

        await(connector.getMtdId(nino)) shouldBe Left(MtdIdLookupConnector.Error(statusCode))
      }
    }
  }

}
