/*
 * Copyright 2026 HM Revenue & Customs
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

package v3.residentialPropertyDisposals.createAmendNonPpd.def1

import api.controllers.validators.RulesValidator
import api.controllers.validators.resolvers.*
import api.models.domain.TaxYear
import api.models.errors.{DateFormatError, MtdError}
import cats.data.Validated
import cats.data.Validated.Invalid
import cats.implicits.*
import common.errors.*
import v3.residentialPropertyDisposals.createAmendNonPpd.def1.model.request.{Def1_CreateAmendCgtResidentialPropertyDisposalsRequestData, Disposal}

import java.time.LocalDate

object Def1_CreateAmendCgtResidentialPropertyDisposalsRulesValidator
    extends RulesValidator[Def1_CreateAmendCgtResidentialPropertyDisposalsRequestData] {

  private val resolveNonNegativeParsedNumber = ResolveParsedNumber()
  private val customerReferenceRegex         = "^[0-9a-zA-Z{À-˿'}\\- _&`():.'^]{1,90}$".r

  def validateBusinessRules(parsed: Def1_CreateAmendCgtResidentialPropertyDisposalsRequestData)
      : Validated[Seq[MtdError], Def1_CreateAmendCgtResidentialPropertyDisposalsRequestData] = {

    import parsed.body.*

    combine(
      disposals.zipWithIndex.traverse_ { case (disposal, index) =>
        validateDisposal(disposal, index, parsed.taxYear)
      }
    ).onSuccess(parsed)
  }

  private def validateAcquisitionAndDisposalDates(acquisitionDate: LocalDate,
                                                  disposalDate: LocalDate,
                                                  taxYear: TaxYear,
                                                  basePath: String): Validated[Seq[MtdError], Unit] = {
    val isAcquisitionDateInvalid = acquisitionDate.isAfter(disposalDate)

    val isDisposalDateInvalid = disposalDate.isBefore(taxYear.startDate) || disposalDate.isAfter(taxYear.endDate)

    val validatedAcquisitionDateRule = if (isAcquisitionDateInvalid) {
      Invalid(List(RuleAcquisitionDatAfterDisposalDate.withPath(basePath)))
    } else {
      valid
    }

    val validatedDisposalDateRule = if (isDisposalDateInvalid) {
      Invalid(List(RuleDisposalDateError.withPath(s"$basePath/disposalDate")))
    } else {
      valid
    }

    combine(validatedAcquisitionDateRule, validatedDisposalDateRule)
  }

  private def validateDisposal(disposal: Disposal, index: Int, taxYear: TaxYear): Validated[Seq[MtdError], Unit] = {
    import disposal.*

    val validatedMandatoryDecimalNumbers = List(
      (disposalProceeds, s"/disposals/$index/disposalProceeds"),
      (acquisitionAmount, s"/disposals/$index/acquisitionAmount")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }

    val validatedOptionalDecimalNumbers = List(
      (improvementCosts, s"/disposals/$index/improvementCosts"),
      (additionalCosts, s"/disposals/$index/additionalCosts"),
      (prfAmount, s"/disposals/$index/prfAmount"),
      (otherReliefAmount, s"/disposals/$index/otherReliefAmount"),
      (lossesFromThisYear, s"/disposals/$index/lossesFromThisYear"),
      (lossesFromPreviousYear, s"/disposals/$index/lossesFromPreviousYear"),
      (amountOfNetGain, s"/disposals/$index/amountOfNetGain"),
      (amountOfNetLoss, s"/disposals/$index/amountOfNetLoss")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }

    val validatedDates = (
      ResolveIsoDate(disposalDate, DateFormatError.withPath(s"/disposals/$index/disposalDate")),
      ResolveIsoDate(completionDate, DateFormatError.withPath(s"/disposals/$index/completionDate")),
      ResolveIsoDate(acquisitionDate, DateFormatError.withPath(s"/disposals/$index/acquisitionDate"))
    ).tupled.andThen { case (disposalDate, completionDate, acquisitionDate) =>
      validateAcquisitionAndDisposalDates(acquisitionDate, disposalDate, taxYear, s"/disposals/$index")
    }

    val validatedCustomerRef: Validated[Seq[MtdError], Option[String]] =
      ResolveStringPattern(customerReference, customerReferenceRegex, CustomerRefFormatError.withPath(s"/disposals/$index/customerReference"))

    val validatedLossOrGains = if (disposal.gainAndLossAreBothSupplied) {
      Invalid(List(RuleAmountGainLossError.withPath(s"/disposals/$index")))
    } else if (disposal.isNetAmountEmpty) {
      Invalid(List(RuleAmountGainLossError.withPath(s"/disposals/$index")))
    } else {
      valid
    }

    combine(validatedMandatoryDecimalNumbers, validatedOptionalDecimalNumbers, validatedDates, validatedCustomerRef, validatedLossOrGains)
  }

}
