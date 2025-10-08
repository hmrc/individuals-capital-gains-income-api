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

package v3.retrieveOtherCgt

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.HeaderNames.ACCEPT
import play.api.http.Status.*
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{WSRequest, WSResponse}
import play.api.test.Helpers.AUTHORIZATION
import shared.models.errors.*
import shared.services.*
import shared.support.IntegrationBaseSpec

class Def2_RetrieveOtherCgtControllerHipISpec extends IntegrationBaseSpec {

  "Calling the 'retrieve other CGT' endpoint" should {
    "return a 200 status code" when {

      "any valid request with a Tax Year Specific (TYS) tax year is made" in new TysHipTest {

        override def taxYear: String = "2025-26"

        override def setupStubs(): StubMapping = {
          AuditStub.audit()
          AuthStub.authorised()
          MtdIdLookupStub.ninoFound(nino)
          DownstreamStub.onSuccess(DownstreamStub.GET, downstreamUri, OK, downstreamResponse)
        }

        val response: WSResponse = await(request.get())
        response.status shouldBe OK
        response.json shouldBe mtdResponse
        response.header("Content-Type") shouldBe Some("application/json")
      }
    }

    "return error according to spec" when {
      "validation error" when {
        def validationErrorTest(requestNino: String, requestTaxYear: String, expectedStatus: Int, expectedBody: MtdError): Unit = {
          s"validation fails with ${expectedBody.code} error" in new TysHipTest {

            override val nino: String    = requestNino
            override val taxYear: String = requestTaxYear

            override def setupStubs(): StubMapping = {
              AuditStub.audit()
              AuthStub.authorised()
              MtdIdLookupStub.ninoFound(nino)
            }

            val response: WSResponse = await(request.get())
            response.status shouldBe expectedStatus
            response.json shouldBe Json.toJson(expectedBody)
            response.header("Content-Type") shouldBe Some("application/json")
          }
        }

        val input = Seq(
          ("AA1123A", "2019-20", BAD_REQUEST, NinoFormatError),
          ("AA123456A", "20177", BAD_REQUEST, TaxYearFormatError),
          ("AA123456A", "2015-17", BAD_REQUEST, RuleTaxYearRangeInvalidError),
          ("AA123456A", "2018-19", BAD_REQUEST, RuleTaxYearNotSupportedError)
        )
        input.foreach(args => (validationErrorTest).tupled(args))
      }

      "downstream service error" when {
        def serviceErrorTest(downstreamStatus: Int, downstreamCode: String, expectedStatus: Int, expectedBody: MtdError): Unit = {
          s"downstream returns an $downstreamCode error and status $downstreamStatus" in new TysHipTest {

            override def setupStubs(): StubMapping = {
              AuditStub.audit()
              AuthStub.authorised()
              MtdIdLookupStub.ninoFound(nino)
              DownstreamStub.onError(DownstreamStub.GET, downstreamUri, downstreamStatus, errorBody(downstreamCode))
            }

            val response: WSResponse = await(request.get())
            response.status shouldBe expectedStatus
            response.json shouldBe Json.toJson(expectedBody)
            response.header("Content-Type") shouldBe Some("application/json")
          }
        }

        def errorBody(code: String): String =
          s"""
             |{
             |  "origin": "HoD",
             |  "response": {
             |    "failures": [
             |      {
             |        "type": "$code",
             |        "reason": "message"
             |      }
             |    ]
             |  }
             |}
            """.stripMargin

        val errors = Seq(
          (BAD_REQUEST, "INVALID_TAXABLE_ENTITY_ID", BAD_REQUEST, NinoFormatError),
          (BAD_REQUEST, "INVALID_TAX_YEAR", BAD_REQUEST, TaxYearFormatError),
          (NOT_FOUND, "NO_DATA_FOUND", NOT_FOUND, NotFoundError),
          (INTERNAL_SERVER_ERROR, "SERVER_ERROR", INTERNAL_SERVER_ERROR, InternalError),
          (SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE", INTERNAL_SERVER_ERROR, InternalError)
        )

        val extraTysErrors = Seq(
          (BAD_REQUEST, "INVALID_CORRELATION_ID", INTERNAL_SERVER_ERROR, InternalError),
          (UNPROCESSABLE_ENTITY, "TAX_YEAR_NOT_SUPPORTED", BAD_REQUEST, RuleTaxYearNotSupportedError)
        )

        (errors ++ extraTysErrors).foreach(args => (serviceErrorTest).tupled(args))
      }
    }
  }

  private trait Test {

    val nino: String = "AA123456A"
    def taxYear: String
    def downstreamUri: String

    val downstreamResponse: JsValue = Json.parse(
      """
        |{
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
        |            "gainsWithBADR": 99999999999.99,
        |            "gainsBeforeLosses": 99999999999.99,
        |            "losses": 99999999999.99,
        |            "claimOrElectionCodes": [
        |                "GHO"
        |            ],
        |            "amountOfNetGain": 99999999999.99,
        |            "rttTaxPaid": 99999999999.99
        |        }
        |    ],
        |    "otherGains": [
        |        {
        |            "assetType": "otherProperty",
        |            "numberOfDisposals": 1,
        |            "assetDescription": "example of this asset",
        |            "companyName": "Bob the Builder",
        |            "companyRegistrationNumber": "11111111",
        |            "acquisitionDate": "2025-04-07",
        |            "disposalDate": "2025-07-10",
        |            "disposalProceeds": 99999999999.99,
        |            "allowableCosts": 99999999999.99,
        |            "gainsWithBADR": 99999999999.99,
        |            "gainsWithINV": 99999999999.99,
        |            "gainsBeforeLosses": 99999999999.99,
        |            "losses": 99999999999.99,
        |            "claimOrElectionCodes": [
        |                "PRR"
        |            ],
        |            "amountOfNetGain": 99999999999.99,
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
        |            "gainsWithBADR": 99999999999.99,
        |            "gainsWithINV": 99999999999.99,
        |            "gainsBeforeLosses": 99999999999.99,
        |            "losses": 99999999999.99,
        |            "claimOrElectionCodes": [
        |                "GHO"
        |            ],
        |            "gainsReportedOnRtt": 99999999999.99,
        |            "gainsExceedingLifetimeLimit": 99999999999.99,
        |            "gainsUnderSEIS": 99999999999.99,
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
        |        "gainsFromQAHCBeforeLosses": 99999999999.99,
        |        "lossesFromQAHC": 99999999999.99
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
        |    "lifeTimeAllowance": {
        |        "lifetimeAllowanceBADR": 99999999999.99,
        |        "lifetimeAllowanceINV": 99999999999.99
        |    }
        |}
        |""".stripMargin
    )

    val mtdResponse: JsValue = Json.parse(
      """
        |{
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
        |                "PRR"
        |            ],
        |            "amountOfNetGain": 99999999999.99,
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
        |""".stripMargin
    )

    def uri: String = s"/other-gains/$nino/$taxYear"

    def setupStubs(): StubMapping

    def request: WSRequest = {
      setupStubs()
      buildRequest(uri)
        .withHttpHeaders(
          (ACCEPT, "application/vnd.hmrc.3.0+json"),
          (AUTHORIZATION, "Bearer 123") // some bearer token
        )
    }

  }

  private trait TysHipTest extends Test {
    def taxYear: String       = "2025-26"
    def downstreamUri: String = s"/itsa/income-tax/v1/25-26/income/disposals/other-gains/$nino"
  }

}
