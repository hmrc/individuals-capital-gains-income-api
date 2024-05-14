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

package v1.controllers.validators

import api.controllers.validators.RulesValidator
import api.controllers.validators.resolvers.{ResolveDateRange, ResolveIsoDate, ResolveParsedNumber}
import api.models.errors._
import cats.data.Validated
import cats.data.Validated._
import cats.implicits._
import v1.models.request.createAmendCgtPpdOverrides._

object CreateAmendCgtPpdOverridesRulesValidator extends RulesValidator[CreateAmendCgtPpdOverridesRequestData] {

  private val resolveNonNegativeParsedNumber = ResolveParsedNumber()
  private val minYear: Int = 1900
  private val maxYear: Int = 2100
  private val resolveDateRange = ResolveDateRange(RuleDateRangeInvalidError).withYearsLimitedTo(minYear, maxYear)
  private val ppdSubmissionIdRegex           = "^[A-Za-z0-9]{12}$"

  def validateBusinessRules(parsed: CreateAmendCgtPpdOverridesRequestData): Validated[Seq[MtdError], CreateAmendCgtPpdOverridesRequestData] = {
    import parsed._
    combine(
      validateBothSuppliedDisposals(body),
      validateSuppliedDisposals(body)
    ).onSuccess(parsed)
  }

  private def validateBothSuppliedDisposals(requestBody: CreateAmendCgtPpdOverridesRequestBody): Validated[Seq[MtdError], Unit] = {
    val BothSuppliedMultiplePropertyValidation = requestBody.multiplePropertyDisposals.fold[Validated[Seq[MtdError], Unit]](Valid(())) { disposals =>
      disposals.zipWithIndex.traverse_ { case (multiplePropertyDisposals, index) =>
        validateBothSuppliedMultipleDisposals(multiplePropertyDisposals, index)
      }
    }
    val BothSuppliedSinglePropertyValidation = requestBody.singlePropertyDisposals.fold[Validated[Seq[MtdError], Unit]](Valid(())) { disposals =>
      disposals.zipWithIndex.traverse_ { case (singlePropertyDisposals, index) =>
        validateBothSuppliedSingleDisposals(singlePropertyDisposals, index)
      }
    }

    combine(BothSuppliedMultiplePropertyValidation, BothSuppliedSinglePropertyValidation)
  }

  private def validateBothSuppliedMultipleDisposals(multiplePropertyDisposals: MultiplePropertyDisposals,
                                                    arrayIndex: Int): Validated[Seq[MtdError], Unit] = {
    if (multiplePropertyDisposals.isBothSupplied) {
      Invalid(List(RuleAmountGainLossError.withPath(s"/multiplePropertyDisposals/$arrayIndex")))
    } else if (multiplePropertyDisposals.isNetAmountEmpty) {
      Invalid(List(RuleAmountGainLossError.withPath(s"/multiplePropertyDisposals/$arrayIndex")))
    } else {
      Valid(())
    }
  }

  private def validateBothSuppliedSingleDisposals(singlePropertyDisposals: SinglePropertyDisposals,
                                                  arrayIndex: Int): Validated[Seq[MtdError], Unit] = {
    if (singlePropertyDisposals.isBothSupplied) {
      Invalid(List(RuleAmountGainLossError.withPath(s"/singlePropertyDisposals/$arrayIndex")))
    } else if (singlePropertyDisposals.isBothEmpty) {
      Invalid(List(RuleAmountGainLossError.withPath(s"/singlePropertyDisposals/$arrayIndex")))
    } else {
      Valid(())
    }
  }

  private def validatePpdSubmissionId(ppdSubmissionId: String, error: MtdError): Validated[Seq[MtdError], Unit] = {
    if (ppdSubmissionId.matches(ppdSubmissionIdRegex)) {
      valid
    } else {
      Invalid(List(error))
    }
  }

  private def validateSuppliedDisposals(requestBody: CreateAmendCgtPpdOverridesRequestBody): Validated[Seq[MtdError], Unit] = {
    val multiplePropertyPpdValidation = requestBody.multiplePropertyDisposals.fold[Validated[Seq[MtdError], Unit]](Valid(())) { disposals =>
      disposals.zipWithIndex.traverse_ { case (multiplePropertyDisposals, index) =>
        validateMultiplePropertyDisposalsPpdId(multiplePropertyDisposals, index)
      }
    }
    val multiplePropertyValuesValidation = requestBody.multiplePropertyDisposals.fold[Validated[Seq[MtdError], Unit]](Valid(())) { disposals =>
      disposals.zipWithIndex.traverse_ { case (multiplePropertyDisposals, index) =>
        validateMultiplePropertyDisposalsValues(multiplePropertyDisposals, index)
      }
    }
    val singlePropertyPpdValidation = requestBody.singlePropertyDisposals.fold[Validated[Seq[MtdError], Unit]](Valid(())) { disposals =>
      disposals.zipWithIndex.traverse_ { case (singlePropertyDisposals, index) =>
        validateSinglePropertyDisposalsPpdId(singlePropertyDisposals, index)
      }
    }
    val singlePropertyValuesValidation = requestBody.singlePropertyDisposals.fold[Validated[Seq[MtdError], Unit]](Valid(())) { disposals =>
      disposals.zipWithIndex.traverse_ { case (singlePropertyDisposals, index) =>
        validateSinglePropertyDisposalsValues(singlePropertyDisposals, index)
      }
    }

    combine(multiplePropertyPpdValidation, multiplePropertyValuesValidation, singlePropertyPpdValidation, singlePropertyValuesValidation)
  }

  private def validateMultiplePropertyDisposalsPpdId(multiplePropertyDisposals: MultiplePropertyDisposals,
                                                     arrayIndex: Int): Validated[Seq[MtdError], Unit] = {
    validatePpdSubmissionId(
      multiplePropertyDisposals.ppdSubmissionId,
      PpdSubmissionIdFormatError.withPath(s"/multiplePropertyDisposals/$arrayIndex/ppdSubmissionId"))

  }

  private def validateMultiplePropertyDisposalsValues(multiplePropertyDisposals: MultiplePropertyDisposals,
                                                      arrayIndex: Int): Validated[Seq[MtdError], Unit] = {
    import multiplePropertyDisposals._
    val validatedNonNegatives = List(
      (amountOfNetGain, s"/multiplePropertyDisposals/$arrayIndex/amountOfNetGain"),
      (amountOfNetLoss, s"/multiplePropertyDisposals/$arrayIndex/amountOfNetLoss")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }
    validatedNonNegatives
  }

  private def validateSinglePropertyDisposalsPpdId(singlePropertyDisposals: SinglePropertyDisposals,
                                                   arrayIndex: Int): Validated[Seq[MtdError], Unit] = {
    validatePpdSubmissionId(
      singlePropertyDisposals.ppdSubmissionId,
      PpdSubmissionIdFormatError.withPath(s"/singlePropertyDisposals/$arrayIndex/ppdSubmissionId"))
  }

  private def validateSinglePropertyDisposalsValues(singlePropertyDisposals: SinglePropertyDisposals,
                                                    arrayIndex: Int): Validated[Seq[MtdError], Unit] = {
    import singlePropertyDisposals._
    val validatedNonNegatives = List(
      (disposalProceeds, s"/singlePropertyDisposals/$arrayIndex/disposalProceeds"),
      (acquisitionAmount, s"/singlePropertyDisposals/$arrayIndex/acquisitionAmount"),
      (improvementCosts, s"/singlePropertyDisposals/$arrayIndex/improvementCosts"),
      (additionalCosts, s"/singlePropertyDisposals/$arrayIndex/additionalCosts"),
      (prfAmount, s"/singlePropertyDisposals/$arrayIndex/prfAmount"),
      (otherReliefAmount, s"/singlePropertyDisposals/$arrayIndex/otherReliefAmount")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }

    val validatedOptionalNonNegatives = List(
      (lossesFromThisYear, s"/singlePropertyDisposals/$arrayIndex/lossesFromThisYear"),
      (lossesFromPreviousYear, s"/singlePropertyDisposals/$arrayIndex/lossesFromPreviousYear"),
      (amountOfNetGain, s"/singlePropertyDisposals/$arrayIndex/amountOfNetGain"),
      (amountOfNetLoss, s"/singlePropertyDisposals/$arrayIndex/amountOfNetLoss")
    ).traverse_ { case (value, path) =>
      resolveNonNegativeParsedNumber(value, path)
    }

    val validatedCompletionDate = {
      val resolveCompletionDate = ResolveIsoDate(DateFormatError.withPath(s"/singlePropertyDisposals/$arrayIndex/completionDate"))
      resolveCompletionDate(completionDate)
    }
    val validatedAcquisitionDate = {
      val resolveAquisitionDate = ResolveIsoDate(DateFormatError.withPath(s"/singlePropertyDisposals/$arrayIndex/acquisitionDate"))
      resolveAquisitionDate(acquisitionDate)
    }

    combine(validatedNonNegatives, validatedOptionalNonNegatives, validatedCompletionDate, validatedAcquisitionDate)
  }

}
