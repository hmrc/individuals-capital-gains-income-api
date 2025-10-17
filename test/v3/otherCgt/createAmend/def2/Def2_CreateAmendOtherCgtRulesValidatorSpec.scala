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

package v3.otherCgt.createAmend.def2

import common.errors.*
import common.utils.JsonErrorValidators
import config.MockAppConfig
import play.api.libs.json.*
import shared.models.domain.{Nino, TaxYear}
import shared.models.errors.*
import support.UnitSpec
import v3.otherCgt.createAmend.def2.model.request.{Def2_CreateAmendOtherCgtRequestBody, Def2_CreateAmendOtherCgtRequestData}

class Def2_CreateAmendOtherCgtRulesValidatorSpec extends UnitSpec with MockAppConfig with JsonErrorValidators {

  private implicit val correlationId: String = "someCorrelationId"

  private val validNino    = "AA123456A"
  private val validTaxYear = "2020-21"

  private val cryptoassetsJson =
    Json.parse(
      s"""
         |{
         |  "numberOfDisposals": 1,
         |  "assetDescription": "description string",
         |  "tokenName": "Name of token",
         |  "acquisitionDate": "2025-08-04",
         |  "disposalDate": "2025-09-04",
         |  "disposalProceeds": 100.11,
         |  "allowableCosts": 100.12,
         |  "gainsWithBadr": 100.13,
         |  "gainsBeforeLosses": 100.14,
         |  "losses": 100.15,
         |  "claimOrElectionCodes": [
         |    "GHO"
         |  ],
         |  "amountOfNetGain": 100.16,
         |  "rttTaxPaid": 100.18
         |}""".stripMargin
    )

  private val otherGainsJson =
    Json.parse(
      s"""
         |{
         |  "assetType": "other-property",
         |  "numberOfDisposals": 1,
         |  "assetDescription": "example of this asset",
         |  "companyName": "Bob the Builder",
         |  "companyRegistrationNumber": "11111111",
         |  "acquisitionDate": "2025-04-07",
         |  "disposalDate": "2025-07-10",
         |  "disposalProceeds": 100.11,
         |  "allowableCosts": 100.12,
         |  "gainsWithBadr": 100.13,
         |  "gainsWithInv": 100.14,
         |  "gainsBeforeLosses": 100.15,
         |  "losses": 100.16,
         |  "claimOrElectionCodes": [
         |    "PRR"
         |  ],
         |  "amountOfNetGain": 100.17,
         |  "rttTaxPaid": 100.19
         |}""".stripMargin
    )

  private val unlistedSharesJson =
    Json.parse(
      s"""
         |{
         |  "numberOfDisposals": 1,
         |  "assetDescription": "My asset",
         |  "companyName": "Bob the Builder",
         |  "companyRegistrationNumber": "11111111",
         |  "acquisitionDate": "2025-04-10",
         |  "disposalDate": "2025-04-12",
         |  "disposalProceeds": 100.11,
         |  "allowableCosts": 100.12,
         |  "gainsWithBadr": 100.13,
         |  "gainsWithInv": 100.14,
         |  "gainsBeforeLosses": 100.15,
         |  "losses": 100.16,
         |  "claimOrElectionCodes": [
         |    "GHO"
         |  ],
         |  "gainsReportedOnRtt": 100.17,
         |  "gainsExceedingLifetimeLimit": 100.18,
         |  "gainsUnderSeis": 100.19,
         |  "lossUsedAgainstGeneralIncome": 100.20,
         |  "eisOrSeisReliefDueCurrentYear": 100.21,
         |  "lossesUsedAgainstGeneralIncomePreviousYear": 100.22,
         |  "eisOrSeisReliefDuePreviousYear": 100.23,
         |  "rttTaxPaid": 100.24
         |}""".stripMargin
    )

  private def requestBodyJson(cryptoassets: JsValue = cryptoassetsJson,
                              otherGains: JsValue = otherGainsJson,
                              unlistedShares: JsValue = unlistedSharesJson) =
    Json
      .parse(
        s"""
       |{
       |    "cryptoassets": [
       |      ${Json.toJson(cryptoassets)}
       |    ],
       |    "otherGains": [
       |      ${Json.toJson(otherGains)}
       |    ],
       |    "unlistedShares": [
       |      ${Json.toJson(unlistedShares)}
       |    ],
       |    "gainExcludedIndexedSecurities": {
       |        "gainsFromExcludedSecurities": 100.11
       |    },
       |    "qualifyingAssetHoldingCompany": {
       |        "gainsFromQahcBeforeLosses": 100.11,
       |        "lossesFromQahc": 100.12
       |    },
       |    "nonStandardGains": {
       |        "attributedGains": 100.11,
       |        "attributedGainsRttTaxPaid": 100.12,
       |        "otherGains": 100.13,
       |        "otherGainsRttTaxPaid": 100.14
       |    },
       |    "losses": {
       |        "broughtForwardLossesUsedInCurrentYear": 100.11,
       |        "setAgainstInYearGains": 100.12,
       |        "setAgainstEarlierYear": 100.13,
       |        "lossesToCarryForward": 100.14
       |    },
       |    "adjustments": {
       |        "adjustmentAmount": 100.11
       |    },
       |    "lifetimeAllowance": {
       |        "lifetimeAllowanceBadr": 100.11,
       |        "lifetimeAllowanceInv": 100.12
       |    }
       |}
""".stripMargin
      )
      .as[JsObject]

  private val parsedNino    = Nino(validNino)
  private val parsedTaxYear = TaxYear.fromMtd(validTaxYear)

  private def validate(nino: String = validNino, taxYear: String = validTaxYear, body: JsValue = requestBodyJson()) =
    new Def2_CreateAmendOtherCgtValidator(nino, taxYear, body).validateAndWrapResult()

  private def error(mtdError: MtdError) = Left(ErrorWrapper(correlationId, mtdError))

  class Test {

    MockedAppConfig.minimumPermittedTaxYear
      .returns(2021)
      .anyNumberOfTimes()

  }

  "validator" should {
    "return the parsed domain object" when {
      "a valid request is supplied with all fields" in new Test {
        validate(validNino, validTaxYear, requestBodyJson()) shouldBe
          Right(Def2_CreateAmendOtherCgtRequestData(parsedNino, parsedTaxYear, requestBodyJson().as[Def2_CreateAmendOtherCgtRequestBody]))
      }
    }

    "return NinoFormatError error" when {
      "an invalid nino is supplied" in new Test {
        validate(nino = "A12344A") shouldBe
          Left(ErrorWrapper(correlationId, NinoFormatError))
      }
    }

    "return RuleIncorrectOrEmptyBodyError error" when {
      "an empty JSON body is submitted" in new Test {
        validate(body = JsObject.empty) shouldBe error(RuleIncorrectOrEmptyBodyError)
      }

      "a non-empty JSON body is submitted without any expected fields" in new Test {
        validate(body = Json.parse("""{"field": "value"}""")) shouldBe error(RuleIncorrectOrEmptyBodyError)
      }

      "the submitted request body has fields incorrect type" in new Test {
        validate(body = requestBodyJson(
          cryptoassets = cryptoassetsJson.update("/disposalDate", JsBoolean(true)),
          otherGains = otherGainsJson,
          unlistedShares = unlistedSharesJson)) shouldBe
          error(RuleIncorrectOrEmptyBodyError.withPaths(Seq("/cryptoassets/0/disposalDate")))
      }

      "the submitted request body has missing mandatory fields" in new Test {
        val emptyCryptoassets: JsValue = Json.parse(
          """
            |{
            |
            |}""".stripMargin
        )

        validate(body = requestBodyJson(cryptoassets = emptyCryptoassets, otherGains = otherGainsJson, unlistedShares = unlistedSharesJson)) shouldBe
          error(
            RuleIncorrectOrEmptyBodyError.withPaths(Seq(
              "/cryptoassets/0/acquisitionDate",
              "/cryptoassets/0/allowableCosts",
              "/cryptoassets/0/assetDescription",
              "/cryptoassets/0/disposalDate",
              "/cryptoassets/0/disposalProceeds",
              "/cryptoassets/0/gainsBeforeLosses",
              "/cryptoassets/0/numberOfDisposals",
              "/cryptoassets/0/tokenName"
            ))
          )
      }

    }

    "return ValueFormatError" when {
      def checkNonNegative(bodyFrom: JsNumber => JsValue, path: String): Unit =
        s"$path" must {
          val error = ValueFormatError.withPath(path)
          behave like disallowsUnsupportedDecimals(bodyFrom, error)
          behave like disallowsNegatives(bodyFrom, error)
        }

      def disallowsUnsupportedDecimals(bodyFrom: JsNumber => JsValue, mtdError: MtdError): Unit =
        "disallows unsupported decimals" in new Test {
          validate(body = bodyFrom(JsNumber(123.456))) shouldBe error(mtdError)
        }

      def disallowsNegatives(bodyFrom: JsNumber => JsValue, mtdError: MtdError): Unit =
        "disallows negatives" in new Test {
          validate(body = bodyFrom(JsNumber(-0.01))) shouldBe error(mtdError)
        }

      "a cryptoassets field is invalid" when {
        def bodyCreator(field: String)(value: JsNumber) =
          requestBodyJson(cryptoassets = cryptoassetsJson.update(field, value), otherGains = otherGainsJson, unlistedShares = unlistedSharesJson)

        Seq(
          "disposalProceeds",
          "allowableCosts",
          "gainsWithBadr",
          "gainsBeforeLosses",
          "losses",
          "amountOfNetGain", // amountOfNetLoss needs doing separately as will fail with different 400 if amountOfNetGain is also included
          "rttTaxPaid"
        ).foreach(field => checkNonNegative(bodyCreator(field), path = s"/cryptoassets/0/$field"))
      }

      "a otherGains field is invalid" when {
        def bodyCreator(field: String)(value: JsNumber) =
          requestBodyJson(cryptoassets = cryptoassetsJson, otherGains = otherGainsJson.update(field, value), unlistedShares = unlistedSharesJson)

        Seq(
          "disposalProceeds",
          "allowableCosts",
          "gainsWithBadr",
          "gainsWithInv",
          "gainsBeforeLosses",
          "losses",
          "amountOfNetGain", // amountOfNetLoss needs doing separately as will fail with different 400 if amountOfNetGain is also included
          "rttTaxPaid"
        ).foreach(field => checkNonNegative(bodyCreator(field), path = s"/otherGains/0/$field"))
      }

      "a unlistedShares field is invalid" when {
        def bodyCreator(field: String)(value: JsNumber) =
          requestBodyJson(cryptoassets = cryptoassetsJson, otherGains = otherGainsJson, unlistedShares = unlistedSharesJson.update(field, value))

        Seq(
          "disposalProceeds",
          "allowableCosts",
          "gainsWithBadr",
          "gainsWithInv",
          "gainsBeforeLosses",
          "losses",
          "gainsReportedOnRtt",
          "gainsExceedingLifetimeLimit",
          "gainsUnderSeis",
          "lossUsedAgainstGeneralIncome",
          "eisOrSeisReliefDueCurrentYear",
          "lossesUsedAgainstGeneralIncomePreviousYear",
          "eisOrSeisReliefDuePreviousYear",
          "rttTaxPaid"
        ).foreach(field => checkNonNegative(bodyCreator(field), path = s"/unlistedShares/0/$field"))
      }

      "a non array field is invalid" when {
        def bodyCreator(field: String)(value: JsNumber) =
          requestBodyJson(cryptoassets = cryptoassetsJson, otherGains = otherGainsJson, unlistedShares = unlistedSharesJson).update(field, value)

        Seq(
          "/gainExcludedIndexedSecurities/gainsFromExcludedSecurities",
          "/qualifyingAssetHoldingCompany/gainsFromQahcBeforeLosses",
          "/qualifyingAssetHoldingCompany/lossesFromQahc",
          "/nonStandardGains/attributedGains",
          "/nonStandardGains/attributedGainsRttTaxPaid",
          "/nonStandardGains/otherGains",
          "/nonStandardGains/otherGainsRttTaxPaid",
          "/losses/broughtForwardLossesUsedInCurrentYear",
          "/losses/setAgainstInYearGains",
          "/losses/setAgainstEarlierYear",
          "/losses/lossesToCarryForward",
          "/adjustments/adjustmentAmount",
          "/lifetimeAllowance/lifetimeAllowanceBadr",
          "/lifetimeAllowance/lifetimeAllowanceInv"
        ).foreach(field => checkNonNegative(bodyCreator(field), path = field))
      }
    }

    "return DateFormatError error" when {
      "supplied dates in cryptoassets are invalid" in new Test {
        val body = requestBodyJson(
          cryptoassets = cryptoassetsJson
            .update("/acquisitionDate", JsString("BAD_DATE"))
            .update("/disposalDate", JsString("BAD_DATE"))
        )

        validate(body = body) shouldBe
          error(
            DateFormatError.withPaths(
              Seq(
                "/cryptoassets/0/acquisitionDate",
                "/cryptoassets/0/disposalDate"
              )))
      }

      "supplied dates in otherGains are invalid" in new Test {
        val body = requestBodyJson(
          otherGains = otherGainsJson
            .update("/acquisitionDate", JsString("BAD_DATE"))
            .update("/disposalDate", JsString("BAD_DATE"))
        )

        validate(body = body) shouldBe
          error(
            DateFormatError.withPaths(
              Seq(
                "/otherGains/0/acquisitionDate",
                "/otherGains/0/disposalDate"
              )))
      }

      "supplied dates in unlistedShares are invalid" in new Test {
        val body = requestBodyJson(
          unlistedShares = unlistedSharesJson
            .update("/acquisitionDate", JsString("BAD_DATE"))
            .update("/disposalDate", JsString("BAD_DATE"))
        )

        validate(body = body) shouldBe
          error(
            DateFormatError.withPaths(
              Seq(
                "/unlistedShares/0/acquisitionDate",
                "/unlistedShares/0/disposalDate"
              )))
      }

      "return AssetTypeFormatError error" when {
        "incorrectAssetType" in new Test {
          validate(body = requestBodyJson(otherGains = otherGainsJson.update("/assetType", JsString("wrong")))) shouldBe
            error(AssetTypeFormatError.withPath("/otherGains/0/assetType"))

        }
      }

      "return AssetDescriptionFormatError error in cryptoassets" when {
        "description is too long" in new Test {
          validate(body = requestBodyJson(cryptoassets = cryptoassetsJson.update("/assetDescription", JsString("A" * 91)))) shouldBe
            error(AssetDescriptionFormatError.withPath("/cryptoassets/0/assetDescription"))
        }

        "description is too short" in new Test {
          validate(body = requestBodyJson(cryptoassets = cryptoassetsJson.update("/assetDescription", JsString("")))) shouldBe
            error(AssetDescriptionFormatError.withPath("/cryptoassets/0/assetDescription"))
        }
      }

      "return TokenNameFormatError error in cryptoassets" when {
        "description is too long" in new Test {
          validate(body = requestBodyJson(cryptoassets = cryptoassetsJson.update("/tokenName", JsString("A" * 91)))) shouldBe
            error(TokenNameFormatError.withPath("/cryptoassets/0/tokenName"))
        }

        "description is too short" in new Test {
          validate(body = requestBodyJson(cryptoassets = cryptoassetsJson.update("/tokenName", JsString("")))) shouldBe
            error(TokenNameFormatError.withPath("/cryptoassets/0/tokenName"))
        }
      }

      "return AssetDescriptionFormatError error in otherGains" when {
        "description is too long" in new Test {
          validate(body = requestBodyJson(otherGains = otherGainsJson.update("/assetDescription", JsString("A" * 91)))) shouldBe
            error(AssetDescriptionFormatError.withPath("/otherGains/0/assetDescription"))
        }

        "description is too short" in new Test {
          validate(body = requestBodyJson(otherGains = otherGainsJson.update("/assetDescription", JsString("")))) shouldBe
            error(AssetDescriptionFormatError.withPath("/otherGains/0/assetDescription"))
        }
      }

      "return CompanyNameFormatError error in otherGains" when {
        "description is too long" in new Test {
          validate(body = requestBodyJson(otherGains = otherGainsJson.update("/companyName", JsString("A" * 161)))) shouldBe
            error(CompanyNameFormatError.withPath("/otherGains/0/companyName"))
        }

        "description is too short" in new Test {
          validate(body = requestBodyJson(otherGains = otherGainsJson.update("/companyName", JsString("")))) shouldBe
            error(CompanyNameFormatError.withPath("/otherGains/0/companyName"))
        }
      }

      "return AssetDescriptionFormatError error in unlistedShares" when {
        "description is too long" in new Test {
          validate(body = requestBodyJson(unlistedShares = unlistedSharesJson.update("/assetDescription", JsString("A" * 91)))) shouldBe
            error(AssetDescriptionFormatError.withPath("/unlistedShares/0/assetDescription"))
        }

        "description is too short" in new Test {
          validate(body = requestBodyJson(unlistedShares = unlistedSharesJson.update("/assetDescription", JsString("")))) shouldBe
            error(AssetDescriptionFormatError.withPath("/unlistedShares/0/assetDescription"))
        }
      }

      "return CompanyNameFormatError error in unlistedShares" when {
        "description is too long" in new Test {
          validate(body = requestBodyJson(unlistedShares = unlistedSharesJson.update("/companyName", JsString("A" * 161)))) shouldBe
            error(CompanyNameFormatError.withPath("/unlistedShares/0/companyName"))
        }

        "description is too short" in new Test {
          validate(body = requestBodyJson(unlistedShares = unlistedSharesJson.update("/companyName", JsString("")))) shouldBe
            error(CompanyNameFormatError.withPath("/unlistedShares/0/companyName"))
        }
      }

      "return RuleGainLossError error" when {
        "both gain and loss are supplied in cryptoassets" in new Test {
          validate(validNino, validTaxYear, requestBodyJson(cryptoassets = cryptoassetsJson.update("/amountOfNetLoss", JsNumber(1.21)))) shouldBe
            error(RuleGainLossError.withPath("/cryptoassets/0"))
        }

        "both amountOfNetGain and amountOfNetLoss are supplied in otherGains" in new Test {
          validate(validNino, validTaxYear, requestBodyJson(otherGains = otherGainsJson.update("/amountOfNetLoss", JsNumber(1.21)))) shouldBe
            error(RuleGainLossError.withPath("/otherGains/0"))
        }
      }

    }

  }

}
