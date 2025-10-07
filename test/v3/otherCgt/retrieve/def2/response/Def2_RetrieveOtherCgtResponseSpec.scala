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

package v3.otherCgt.retrieve.def2.response

import play.api.libs.json.{JsError, JsObject, JsValue, Json}
import shared.models.domain.Timestamp
import support.UnitSpec
import v3.otherCgt.retrieve.def2.model.response.*

class Def2_RetrieveOtherCgtResponseSpec extends UnitSpec {

  val validResponseJson: JsValue = Json.parse(
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

  val validDownstreamResponseJson: JsValue = Json.parse(
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
      |            "gainsWithBADR": 99999999999.99,
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
     """.stripMargin
  )

  val minimumValidResponseJson: JsValue = Json.parse(
    """
      |{
      |    "submittedOn": "2026-02-07T16:18:44.403Z"
      |}
     """.stripMargin
  )

  val invalidJson: JsValue = JsObject.empty

  val responseModel: Def2_RetrieveOtherCgtResponse = Def2_RetrieveOtherCgtResponse(
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
          claimOrElectionCodes = Some(Seq(CryptoassetsClaimOrElectionCodes.GHO)),
          amountOfNetGain = Some(99999999999.99),
          amountOfNetLoss = Some(99999999999.99),
          rttTaxPaid = Some(99999999999.99)
        )
      )),
    otherGains = Some(
      Seq(
        OtherGains(
          assetType = "other-property",
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
          claimOrElectionCodes = Some(Seq(OtherGainsClaimOrElectionCodes.GHO)),
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
          claimOrElectionCodes = Some(Seq(UnlistedSharesClaimOrElectionCodes.GHO)),
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
      NonStandardGains(
        attributedGains = Some(99999999999.99),
        attributedGainsRttTaxPaid = Some(99999999999.99),
        otherGains = Some(99999999999.99),
        otherGainsRttTaxPaid = Some(99999999999.99)
      )
    ),
    losses = Some(
      Losses(
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

  val downstreamResponseModel: Def2_RetrieveOtherCgtResponse = Def2_RetrieveOtherCgtResponse(
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
          claimOrElectionCodes = Some(Seq(CryptoassetsClaimOrElectionCodes.GHO)),
          amountOfNetGain = Some(99999999999.99),
          amountOfNetLoss = Some(99999999999.99),
          rttTaxPaid = Some(99999999999.99)
        )
      )),
    otherGains = Some(
      Seq(
        OtherGains(
          assetType = "other-property",
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
          claimOrElectionCodes = Some(Seq(OtherGainsClaimOrElectionCodes.GHO)),
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
          claimOrElectionCodes = Some(Seq(UnlistedSharesClaimOrElectionCodes.GHO)),
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
      NonStandardGains(
        attributedGains = Some(99999999999.99),
        attributedGainsRttTaxPaid = Some(99999999999.99),
        otherGains = Some(99999999999.99),
        otherGainsRttTaxPaid = Some(99999999999.99)
      )
    ),
    losses = Some(
      Losses(
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

  val minimumResponseModel: Def2_RetrieveOtherCgtResponse = Def2_RetrieveOtherCgtResponse(
    submittedOn = Timestamp("2026-02-07T16:18:44.403Z"),
    cryptoassets = None,
    otherGains = None,
    unlistedShares = None,
    gainExcludedIndexedSecurities = None,
    qualifyingAssetHoldingCompany = None,
    nonStandardGains = None,
    losses = None,
    adjustments = None,
    lifetimeAllowance = None
  )

  "Def2_RetrieveOtherCgtResponse" when {
    "read from valid JSON" should {
      "produce the expected response model" in {
        validDownstreamResponseJson.as[Def2_RetrieveOtherCgtResponse] shouldBe downstreamResponseModel
      }
    }

    "read from the minimum valid JSON" should {
      "produce the expected response model" in {
        minimumValidResponseJson.as[Def2_RetrieveOtherCgtResponse] shouldBe minimumResponseModel
      }
    }

    "read from invalid JSON" should {
      "produce a JsError" in {
        invalidJson.validate[Def2_RetrieveOtherCgtResponse] shouldBe a[JsError]
      }
    }

    "written to JSON" should {
      "produce the expected JSON" in {
        Json.toJson(responseModel) shouldBe validResponseJson
      }
    }
  }

}
