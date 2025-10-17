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

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.implicits.*
import common.errors.*
import shared.controllers.validators.RulesValidator
import shared.controllers.validators.resolvers.{ResolveIsoDate, ResolveParsedNumber}
import shared.models.errors.{DateFormatError, MtdError}
import v3.otherCgt.createAmend.def2.model.request.*

object Def2_CreateAmendOtherCgtRulesValidator extends RulesValidator[Def2_CreateAmendOtherCgtRequestData] {

  private val resolveNonNegativeParsedNumber = ResolveParsedNumber()
  private val resolveNumberOfDisposals       = ResolveParsedNumber(min = 1)
  private val regex                          = "^[0-9a-zA-Z{À-˿'}\\- _&`():.'^]{1,90}$".r
  private val companyNameRegex               = "^.{0,160}$".r

  def validateBusinessRules(parsed: Def2_CreateAmendOtherCgtRequestData): Validated[Seq[MtdError], Def2_CreateAmendOtherCgtRequestData] = {

    import parsed.*

    combine(
      validateCryptoassetSequence(body),
      validateOtherGainsSequence(body),
      validateUnlistedSharesSequence(body),
      validateGainExcludedIndexedSecurities(body),
      validateQualifyingAssetHoldingCompany(body),
      validateNonStandardGains(body),
      validateLosses(body),
      validateAdjustments(body),
      validateLifetimeAllowance(body)
    ).onSuccess(parsed)
  }

  private def validateCryptoassetSequence(requestBody: Def2_CreateAmendOtherCgtRequestBody): Validated[Seq[MtdError], Unit] = {
    requestBody.cryptoassets.fold[Validated[Seq[MtdError], Unit]](Valid(())) { cryptoassets =>
      cryptoassets.zipWithIndex.traverse_ { case (cryptoassets, index) =>
        validateCryptoassets(cryptoassets, index)
      }
    }
  }

  private def validateCryptoassets(cryptoassets: Cryptoasset, index: Int): Validated[Seq[MtdError], Unit] = {
    import cryptoassets.*

    val validatedMandatoryDecimalNumbers = List(
      (disposalProceeds, s"/cryptoassets/$index/disposalProceeds"),
      (allowableCosts, s"/cryptoassets/$index/allowableCosts"),
      (gainsBeforeLosses, s"/cryptoassets/$index/gainsBeforeLosses")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }

    val validatedOptionalDecimalNumbers = List(
      (gainsWithBadr, s"/cryptoassets/$index/gainsWithBadr"),
      (losses, s"/cryptoassets/$index/losses"),
      (amountOfNetGain, s"/cryptoassets/$index/amountOfNetGain"),
      (amountOfNetLoss, s"/cryptoassets/$index/amountOfNetLoss"),
      (rttTaxPaid, s"/cryptoassets/$index/rttTaxPaid")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }

    val validatedNumberOfDisposals = List(
      (numberOfDisposals, s"/cryptoassets/$index/numberOfDisposals")
    ).traverse_ { case (value, path) =>
      resolveNumberOfDisposals(value, path)
    }

    val validatedDates = List(
      (acquisitionDate, s"/cryptoassets/$index/acquisitionDate"),
      (disposalDate, s"/cryptoassets/$index/disposalDate")
    ).traverse_ { case (value, path) =>
      val resolveDate = ResolveIsoDate(DateFormatError.withPath(path))
      resolveDate(value)
    }

    val validatedAssetDescription =
      ResolveAssetDescription(assetDescription, regex, AssetDescriptionFormatError.withPath(s"/cryptoassets/$index/assetDescription"))

    val validatedTokenName =
      ResolveAssetDescription(tokenName, regex, TokenNameFormatError.withPath(s"/cryptoassets/$index/tokenName"))

    val validatedClaimOrElectionCodes = claimOrElectionCodes match {
      case Some(values) =>
        combine(
          values.zipWithIndex.traverse_ { case (value, subIndex) =>
            ResolveClaimOrElectionCodes(value, ClaimOrElectionCodesFormatError.withPath(s"/cryptoassets/$index/claimOrElectionCodes/$subIndex"))
          }
        )
      case None => valid
    }

    val validatedLossOrGains = if (cryptoassets.gainAndLossBothSupplied) {
      Invalid(List(RuleGainLossError.copy(paths = Some(Seq(s"/cryptoassets/$index")))))
    } else {
      valid
    }

    combine(
      validatedMandatoryDecimalNumbers,
      validatedOptionalDecimalNumbers,
      validatedNumberOfDisposals,
      validatedDates,
      validatedLossOrGains,
      validatedAssetDescription,
      validatedTokenName,
      validatedClaimOrElectionCodes
    )
  }

  private def validateOtherGainsSequence(requestBody: Def2_CreateAmendOtherCgtRequestBody): Validated[Seq[MtdError], Unit] = {
    requestBody.otherGains.fold[Validated[Seq[MtdError], Unit]](Valid(())) { otherGains =>
      otherGains.zipWithIndex.traverse_ { case (otherGains, index) =>
        validateOtherGains(otherGains, index)
      }
    }
  }

  private def validateOtherGains(otherGains: OtherGain, index: Int): Validated[Seq[MtdError], Unit] = {
    import otherGains.*

    val validatedMandatoryDecimalNumbers = List(
      (disposalProceeds, s"/otherGains/$index/disposalProceeds"),
      (allowableCosts, s"/otherGains/$index/allowableCosts"),
      (gainsBeforeLosses, s"/otherGains/$index/gainsBeforeLosses")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }

    val validatedOptionalDecimalNumbers = List(
      (gainsWithBadr, s"/otherGains/$index/gainsWithBadr"),
      (gainsWithInv, s"/otherGains/$index/gainsWithInv"),
      (losses, s"/otherGains/$index/losses"),
      (amountOfNetGain, s"/otherGains/$index/amountOfNetGain"),
      (amountOfNetLoss, s"/otherGains/$index/amountOfNetLoss"),
      (rttTaxPaid, s"/otherGains/$index/rttTaxPaid")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }

    val validatedNumberOfDisposals = List(
      (numberOfDisposals, s"/otherGains/$index/numberOfDisposals")
    ).traverse_ { case (value, path) =>
      resolveNumberOfDisposals(value, path)
    }

    val validatedDates = List(
      (acquisitionDate, s"/otherGains/$index/acquisitionDate"),
      (disposalDate, s"/otherGains/$index/disposalDate")
    ).traverse_ { case (value, path) =>
      val resolveDate = ResolveIsoDate(DateFormatError.withPath(path))
      resolveDate(value)
    }

    val validatedAssetDescription =
      ResolveAssetDescription(assetDescription, regex, AssetDescriptionFormatError.withPath(s"/otherGains/$index/assetDescription"))

    val validatedCompanyName =
      ResolveCompanyName(companyName, companyNameRegex, CompanyNameFormatError.withPath(s"/otherGains/$index/companyName"))

    val validatedClaimOrElectionCodes = claimOrElectionCodes match {
      case Some(values) =>
        combine(
          values.zipWithIndex.traverse_ { case (value, subIndex) =>
            ResolveClaimOrElectionCodes(value, ClaimOrElectionCodesFormatError.withPath(s"/otherGains/$index/claimOrElectionCodes/$subIndex"))
          }
        )
      case None => valid
    }

    val validatedAssetType = ResolveAssetType(assetType, AssetTypeFormatError.withPath(s"/otherGains/$index/assetType"))

    val validatedLossOrGains = if (otherGains.gainAndLossBothSupplied) {
      Invalid(List(RuleGainLossError.copy(paths = Some(Seq(s"/otherGains/$index")))))
    } else {
      valid
    }

    combine(
      validatedMandatoryDecimalNumbers,
      validatedOptionalDecimalNumbers,
      validatedNumberOfDisposals,
      validatedDates,
      validatedAssetDescription,
      validatedCompanyName,
      validatedClaimOrElectionCodes,
      validatedAssetType,
      validatedLossOrGains
    )
  }

  private def validateUnlistedSharesSequence(requestBody: Def2_CreateAmendOtherCgtRequestBody): Validated[Seq[MtdError], Unit] = {
    requestBody.unlistedShares.fold[Validated[Seq[MtdError], Unit]](Valid(())) { unlistedShares =>
      unlistedShares.zipWithIndex.traverse_ { case (unlistedShares, index) =>
        validateUnlistedShares(unlistedShares, index)
      }
    }
  }

  private def validateUnlistedShares(unlistedShares: UnlistedShare, index: Int): Validated[Seq[MtdError], Unit] = {
    import unlistedShares.*

    val validatedMandatoryDecimalNumbers = List(
      (disposalProceeds, s"/unlistedShares/$index/disposalProceeds"),
      (allowableCosts, s"/unlistedShares/$index/allowableCosts"),
      (gainsBeforeLosses, s"/unlistedShares/$index/gainsBeforeLosses")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }

    val validatedOptionalDecimalNumbers = List(
      (gainsWithBadr, s"/unlistedShares/$index/gainsWithBadr"),
      (gainsWithInv, s"/unlistedShares/$index/gainsWithInv"),
      (losses, s"/unlistedShares/$index/losses"),
      (gainsReportedOnRtt, s"/unlistedShares/$index/gainsReportedOnRtt"),
      (gainsExceedingLifetimeLimit, s"/unlistedShares/$index/gainsExceedingLifetimeLimit"),
      (gainsUnderSeis, s"/unlistedShares/$index/gainsUnderSeis"),
      (lossUsedAgainstGeneralIncome, s"/unlistedShares/$index/lossUsedAgainstGeneralIncome"),
      (eisOrSeisReliefDueCurrentYear, s"/unlistedShares/$index/eisOrSeisReliefDueCurrentYear"),
      (lossesUsedAgainstGeneralIncomePreviousYear, s"/unlistedShares/$index/lossesUsedAgainstGeneralIncomePreviousYear"),
      (eisOrSeisReliefDuePreviousYear, s"/unlistedShares/$index/eisOrSeisReliefDuePreviousYear"),
      (rttTaxPaid, s"/unlistedShares/$index/rttTaxPaid")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }

    val validatedNumberOfDisposals = List(
      (numberOfDisposals, s"/unlistedShares/$index/numberOfDisposals")
    ).traverse_ { case (value, path) =>
      resolveNumberOfDisposals(value, path)
    }

    val validatedDates = List(
      (acquisitionDate, s"/unlistedShares/$index/acquisitionDate"),
      (disposalDate, s"/unlistedShares/$index/disposalDate")
    ).traverse_ { case (value, path) =>
      val resolveDate = ResolveIsoDate(DateFormatError.withPath(path))
      resolveDate(value)
    }

    val validatedAssetDescription =
      ResolveAssetDescription(assetDescription, regex, AssetDescriptionFormatError.withPath(s"/unlistedShares/$index/assetDescription"))

    val validatedCompanyName =
      ResolveCompanyName(companyName, companyNameRegex, CompanyNameFormatError.withPath(s"/unlistedShares/$index/companyName"))

    val validatedClaimOrElectionCodes = claimOrElectionCodes match {
      case Some(values) =>
        combine(
          values.zipWithIndex.traverse_ { case (value, subIndex) =>
            ResolveClaimOrElectionCodes(value, ClaimOrElectionCodesFormatError.withPath(s"/unlistedShares/$index/claimOrElectionCodes/$subIndex"))
          }
        )
      case None => valid
    }

    combine(
      validatedMandatoryDecimalNumbers,
      validatedOptionalDecimalNumbers,
      validatedNumberOfDisposals,
      validatedDates,
      validatedAssetDescription,
      validatedCompanyName,
      validatedClaimOrElectionCodes
    )
  }

  private def validateGainExcludedIndexedSecurities(requestBody: Def2_CreateAmendOtherCgtRequestBody): Validated[Seq[MtdError], Unit] = {
    requestBody.gainExcludedIndexedSecurities.map(validateGainExcludedIndexedSecurities).getOrElse(valid)
  }

  private def validateGainExcludedIndexedSecurities(gainExcludedIndexedSecurities: GainExcludedIndexedSecurities): Validated[Seq[MtdError], Unit] = {
    import gainExcludedIndexedSecurities.*

    List(
      (gainsFromExcludedSecurities, "/gainExcludedIndexedSecurities/gainsFromExcludedSecurities")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }
  }

  private def validateQualifyingAssetHoldingCompany(requestBody: Def2_CreateAmendOtherCgtRequestBody): Validated[Seq[MtdError], Unit] = {
    requestBody.qualifyingAssetHoldingCompany.map(validateQualifyingAssetHoldingCompany).getOrElse(valid)
  }

  private def validateQualifyingAssetHoldingCompany(qualifyingAssetHoldingCompany: QualifyingAssetHoldingCompany): Validated[Seq[MtdError], Unit] = {
    import qualifyingAssetHoldingCompany.*

    List(
      (gainsFromQahcBeforeLosses, "/qualifyingAssetHoldingCompany/gainsFromQahcBeforeLosses"),
      (lossesFromQahc, "/qualifyingAssetHoldingCompany/lossesFromQahc")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }
  }

  private def validateNonStandardGains(requestBody: Def2_CreateAmendOtherCgtRequestBody): Validated[Seq[MtdError], Unit] = {
    requestBody.nonStandardGains.map(validateNonStandardGains).getOrElse(valid)
  }

  private def validateNonStandardGains(nonStandardGains: NonStandardGains): Validated[Seq[MtdError], Unit] = {
    import nonStandardGains.*

    List(
      (attributedGains, "/nonStandardGains/attributedGains"),
      (attributedGainsRttTaxPaid, "/nonStandardGains/attributedGainsRttTaxPaid"),
      (otherGains, "/nonStandardGains/otherGains"),
      (otherGainsRttTaxPaid, "/nonStandardGains/otherGainsRttTaxPaid")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }
  }

  private def validateLosses(requestBody: Def2_CreateAmendOtherCgtRequestBody): Validated[Seq[MtdError], Unit] = {
    requestBody.losses.map(validateLosses).getOrElse(valid)
  }

  private def validateLosses(losses: Losses): Validated[Seq[MtdError], Unit] = {
    import losses.*

    List(
      (broughtForwardLossesUsedInCurrentYear, "/losses/broughtForwardLossesUsedInCurrentYear"),
      (setAgainstInYearGains, "/losses/setAgainstInYearGains"),
      (setAgainstEarlierYear, "/losses/setAgainstEarlierYear"),
      (lossesToCarryForward, "/losses/lossesToCarryForward")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }
  }

  private def validateAdjustments(requestBody: Def2_CreateAmendOtherCgtRequestBody): Validated[Seq[MtdError], Unit] = {
    requestBody.adjustments.map(validateAdjustments).getOrElse(valid)
  }

  private def validateAdjustments(adjustments: Adjustments): Validated[Seq[MtdError], Unit] = {
    import adjustments.*

    List(
      (adjustmentAmount, "/adjustments/adjustmentAmount")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }
  }

  private def validateLifetimeAllowance(requestBody: Def2_CreateAmendOtherCgtRequestBody): Validated[Seq[MtdError], Unit] = {
    requestBody.lifetimeAllowance.map(validateLifetimeAllowance).getOrElse(valid)
  }

  private def validateLifetimeAllowance(lifetimeAllowance: LifetimeAllowance): Validated[Seq[MtdError], Unit] = {
    import lifetimeAllowance.*

    List(
      (lifetimeAllowanceBadr, "/lifetimeAllowance/lifetimeAllowanceBadr"),
      (lifetimeAllowanceInv, "/lifetimeAllowance/lifetimeAllowanceInv")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }
  }

}
