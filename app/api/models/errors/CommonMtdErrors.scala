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

package api.models.errors

import play.api.http.Status._

object NinoFormatError         extends MtdError("FORMAT_NINO", "The provided NINO is invalid", BAD_REQUEST)
object TaxYearFormatError      extends MtdError("FORMAT_TAX_YEAR", "The provided tax year is invalid", BAD_REQUEST)
object EmploymentIdFormatError extends MtdError("FORMAT_EMPLOYMENT_ID", "The provided employment ID is invalid", BAD_REQUEST)

object CountryCodeFormatError extends MtdError("FORMAT_COUNTRY_CODE", "The format of the country code is invalid", BAD_REQUEST)
object CountryCodeRuleError   extends MtdError("RULE_COUNTRY_CODE", "The country code is not a valid ISO 3166-1 alpha-3 country code", BAD_REQUEST)
object DateFormatError        extends MtdError("FORMAT_DATE", "The field should be in the format YYYY-MM-DD", BAD_REQUEST)

object ValueFormatError extends MtdError("FORMAT_VALUE", "The value must be between 0 and 99999999999.99", BAD_REQUEST) {

  def forPathAndRange(path: String, min: String, max: String): MtdError =
    ValueFormatError.copy(paths = Some(Seq(path)), message = s"The value must be between $min and $max")

}

object BusinessIdFormatError extends MtdError("FORMAT_BUSINESS_ID", "The Business ID format is invalid", BAD_REQUEST)

object CalculationIdFormatError extends MtdError("FORMAT_CALCULATION_ID", "The provided calculation ID is invalid", BAD_REQUEST)

object AccountNameFormatError      extends MtdError("FORMAT_ACCOUNT_NAME", "The provided account name is invalid", BAD_REQUEST)
object PpdSubmissionIdFormatError  extends MtdError("FORMAT_PPD_SUBMISSION_ID", "The provided ppdSubmissionId is invalid", BAD_REQUEST)
object CustomerRefFormatError      extends MtdError("FORMAT_CUSTOMER_REF", "The provided customer reference is invalid", BAD_REQUEST)
object AssetDescriptionFormatError extends MtdError("FORMAT_ASSET_DESCRIPTION", "The provided asset description is invalid", BAD_REQUEST)
object AssetTypeFormatError        extends MtdError("FORMAT_ASSET_TYPE", "The format of the assetType value is invalid", BAD_REQUEST)
object SourceFormatError           extends MtdError("FORMAT_SOURCE", "The provided source is invalid", BAD_REQUEST)
object StartDateFormatError        extends MtdError("FORMAT_START_DATE", "The provided start date is invalid", BAD_REQUEST)
object EndDateFormatError          extends MtdError("FORMAT_END_DATE", "The provided End date is invalid", BAD_REQUEST)
object CessationDateFormatError    extends MtdError("FORMAT_CESSATION_DATE", "The provided cessation date is invalid", BAD_REQUEST)

object ClaimOrElectionCodesFormatError
    extends MtdError("FORMAT_CLAIM_OR_ELECTION_CODES", "The format of the claimOrElectionCodes value is invalid", BAD_REQUEST)

object TransactionIdFormatError extends MtdError(code = "FORMAT_TRANSACTION_ID", message = "The transaction ID format is invalid", BAD_REQUEST)

object RuleDateRangeInvalidError
    extends MtdError("RULE_DATE_RANGE_INVALID", "The date specified does not lie within the supported range", BAD_REQUEST)
// Rule Errors

object RuleTaxYearNotSupportedError
    extends MtdError("RULE_TAX_YEAR_NOT_SUPPORTED", "The tax year specified does not lie within the supported range", BAD_REQUEST)

object RuleIncorrectOrEmptyBodyError
    extends MtdError("RULE_INCORRECT_OR_EMPTY_BODY_SUBMITTED", "An empty or non-matching body was submitted", BAD_REQUEST)

object RuleTaxYearRangeInvalidError
    extends MtdError("RULE_TAX_YEAR_RANGE_INVALID", "Tax year range invalid. A tax year range of one year is required", BAD_REQUEST)

object RuleMaximumSavingsAccountsLimitError
    extends MtdError("RULE_MAXIMUM_SAVINGS_ACCOUNTS_LIMIT", "The 1000 savings account limit exceeded", BAD_REQUEST)

object RuleDuplicatedPpdSubmissionIdError
    extends MtdError("RULE_DUPLICATED_PPD_SUBMISSION_ID", "A provided ppdSubmissionId is duplicated", BAD_REQUEST) {

  def forDuplicatedIdAndPaths(id: String, paths: Seq[String]): MtdError =
    RuleDuplicatedPpdSubmissionIdError.copy(message = s"The ppdSubmissionId '$id' is duplicated", paths = Some(paths))

}

object RuleIncorrectDisposalTypeError
    extends MtdError("RULE_INCORRECT_DISPOSAL_TYPE", "A provided ppdSubmissionId is being used for the incorrect disposal type", BAD_REQUEST)

object RuleGainLossError extends MtdError("RULE_GAIN_LOSS", "Only one of gain or loss values can be provided", BAD_REQUEST)

object RuleDisposalDateError
    extends MtdError("RULE_DISPOSAL_DATE", "The disposalDate must be in the specified tax year and no later than today's date", BAD_REQUEST)

object RuleDisposalDateErrorV1 extends MtdError("RULE_DISPOSAL_DATE", "The disposalDate must be within the specified tax year", BAD_REQUEST)

object RuleCompletionDateError
    extends MtdError(
      "RULE_COMPLETION_DATE",
      "The completionDate must be within the specific tax year and not in the future. If the specified tax year has not ended, the completionDate must be between 7th March and 5th April",
      BAD_REQUEST)

object RuleAcquisitionDateAfterDisposalDateError
    extends MtdError("RULE_ACQUISITION_DATE_AFTER_DISPOSAL_DATE", "The acquisitionDate must not be later than disposalDate", BAD_REQUEST)

object RuleGainAfterReliefLossAfterReliefError
    extends MtdError("RULE_GAIN_AFTER_RELIEF_LOSS_AFTER_RELIEF", "Only one of gainAfterRelief or lossAfterRelief values can be provided", BAD_REQUEST)

object RuleAcquisitionDateError extends MtdError("RULE_ACQUISITION_DATE", "The acquisitionDate must not be later than disposalDate", BAD_REQUEST)

object RuleDisposalDateNotFutureError
    extends MtdError(
      "RULE_DISPOSAL_DATE_NOT_FUTURE",
      "The disposalDate must be in the specified tax year and no later than today's date",
      BAD_REQUEST)

object RuleAmountGainLossError
    extends MtdError("RULE_AMOUNT_GAIN_LOSS", "Either amountOfGain or amountOfLoss, must be provided but not both", BAD_REQUEST)

object RuleCessationDateBeforeStartDateError
    extends MtdError("RULE_CESSATION_DATE_BEFORE_START_DATE", "The cessation date cannot be earlier than the start date", BAD_REQUEST)

object RuleEndBeforeStartDateError
    extends MtdError("RULE_END_DATE_BEFORE_START_DATE", "The supplied accounting period end date is before the start date", BAD_REQUEST)

object RuleCountryCodeError extends MtdError("RULE_COUNTRY_CODE", "The country code is not a valid ISO 3166-1 alpha-3 country code", BAD_REQUEST)

object RuleStartDateAfterTaxYearEndError
    extends MtdError("RULE_START_DATE_AFTER_TAX_YEAR_END", "The start date cannot be later than the tax year end", BAD_REQUEST)

object RuleCessationDateBeforeTaxYearStartError
    extends MtdError("RULE_CESSATION_DATE_BEFORE_TAX_YEAR_START", "The cessation date cannot be before the tax year starts", BAD_REQUEST)

object RuleTaxYearNotEndedError extends MtdError("RULE_TAX_YEAR_NOT_ENDED", "Tax year not ended", BAD_REQUEST)

object RuleRequestCannotBeFulfilledError
    extends MtdError("RULE_REQUEST_CANNOT_BE_FULFILLED", "Custom (will vary in production depending on the actual error)", 422)

//Stub errors
object RuleIncorrectGovTestScenarioError extends MtdError("RULE_INCORRECT_GOV_TEST_SCENARIO", "The Gov-Test-Scenario was not found", BAD_REQUEST)

// Not found errors
object PpdSubmissionIdNotFoundError extends MtdError("PPD_SUBMISSION_ID_NOT_FOUND", "Matching resource not found", NOT_FOUND)

// Standard Errors
object NotFoundError   extends MtdError("MATCHING_RESOURCE_NOT_FOUND", "Matching resource not found", NOT_FOUND)
object InternalError   extends MtdError("INTERNAL_SERVER_ERROR", "An internal server error occurred", INTERNAL_SERVER_ERROR)
object BadRequestError extends MtdError("INVALID_REQUEST", "Invalid request", BAD_REQUEST)
object BVRError        extends MtdError("BUSINESS_ERROR", "Business validation error", BAD_REQUEST)

// Authentication OK but not allowed access to the requested resource
object ClientOrAgentNotAuthorisedError extends MtdError("CLIENT_OR_AGENT_NOT_AUTHORISED", "The client and/or agent is not authorised", FORBIDDEN) {
  def withStatus401: MtdError = copy(httpStatus = UNAUTHORIZED)
}

object InvalidBearerTokenError extends MtdError("UNAUTHORIZED", "Bearer token is missing or not authorized", UNAUTHORIZED)

// Accept header Errors
object InvalidAcceptHeaderError extends MtdError("ACCEPT_HEADER_INVALID", "The accept header is missing or invalid", NOT_ACCEPTABLE)
object UnsupportedVersionError  extends MtdError("NOT_FOUND", "The requested resource could not be found", NOT_FOUND)
object InvalidBodyTypeError     extends MtdError("INVALID_BODY_TYPE", "Expecting text/json or application/json body", UNSUPPORTED_MEDIA_TYPE)

object InvalidTaxYearParameterError
    extends MtdError(code = "INVALID_TAX_YEAR_PARAMETER", message = "A tax year before 2023-24 was supplied", BAD_REQUEST)
