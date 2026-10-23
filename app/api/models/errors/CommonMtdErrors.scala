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

package api.models.errors

import play.api.http.Status.*

// MtdError types that are common across MTD APIs.

// Format Errors
object NinoFormatError extends MtdError("FORMAT_NINO", "The NINO format is invalid", BAD_REQUEST)

object TaxYearFormatError extends MtdError("FORMAT_TAX_YEAR", "The taxYear format is invalid", BAD_REQUEST)

object StartDateFormatError extends MtdError("FORMAT_START_DATE", "The provided Start date is invalid", BAD_REQUEST)

object DateFormatError extends MtdError(code = "FORMAT_DATE", message = "The supplied date format is not valid", BAD_REQUEST)

object ValueFormatError extends MtdError("FORMAT_VALUE", "The value must be between 0 and 99999999999.99", BAD_REQUEST) {

  def forPathAndRange(path: String, min: String, max: String): MtdError =
    ValueFormatError.copy(paths = Some(Seq(path)), message = s"The value must be between $min and $max")

  def forIntegerPathAndRange(path: String, min: String, max: String): MtdError =
    ValueFormatError.copy(paths = Some(Seq(path)), message = s"The value must be an integer between $min and $max")

}

object PpdSubmissionIdFormatError extends MtdError("FORMAT_PPD_SUBMISSION_ID", "The provided ppdSubmissionId is invalid", BAD_REQUEST)

object CustomerRefFormatError extends MtdError("FORMAT_CUSTOMER_REF", "The provided customer reference is invalid", BAD_REQUEST)

object AssetDescriptionFormatError extends MtdError("FORMAT_ASSET_DESCRIPTION", "The provided asset description is invalid", BAD_REQUEST)

object AssetTypeFormatError extends MtdError("FORMAT_ASSET_TYPE", "The format of the assetType value is invalid", BAD_REQUEST)

object TokenNameFormatError extends MtdError("FORMAT_TOKEN_NAME", "The provided tokenName is invalid", BAD_REQUEST)

object CompanyNameFormatError extends MtdError("FORMAT_COMPANY_NAME", "The provided companyName is invalid", BAD_REQUEST)

object CompanyRegistrationNumberFormatError
    extends MtdError("FORMAT_COMPANY_REGISTRATION_NUMBER", "The provided companyRegistrationNumber is invalid", BAD_REQUEST)

object SourceFormatError extends MtdError("FORMAT_SOURCE", "The provided source is invalid", BAD_REQUEST)

object ClaimOrElectionCodesFormatError
    extends MtdError("FORMAT_CLAIM_OR_ELECTION_CODES", "The format of the claimOrElectionCodes value is invalid", BAD_REQUEST)

//Standard Errors
object NotFoundError extends MtdError("MATCHING_RESOURCE_NOT_FOUND", "Matching resource not found", NOT_FOUND)

object InternalError extends MtdError("INTERNAL_SERVER_ERROR", "An internal server error occurred", INTERNAL_SERVER_ERROR)

object BadRequestError extends MtdError("INVALID_REQUEST", "Invalid request", BAD_REQUEST)

object GatewayTimeoutError extends MtdError("GATEWAY_TIMEOUT", "The request has timed out", GATEWAY_TIMEOUT)

object BVRError extends MtdError("BUSINESS_ERROR", "Business validation error", BAD_REQUEST)

object InvalidHttpMethodError extends MtdError("INVALID_HTTP_METHOD", "Invalid HTTP method", METHOD_NOT_ALLOWED)

object InvalidBodyTypeError extends MtdError("INVALID_BODY_TYPE", "Expecting text/json or application/json body", UNSUPPORTED_MEDIA_TYPE)

//Authentication/Authorisation errors

/** Authentication OK but not allowed access to the requested resource
  */
object ClientOrAgentNotAuthorisedError extends MtdError("CLIENT_OR_AGENT_NOT_AUTHORISED", "The client or agent is not authorised", FORBIDDEN) {
  def withStatus401: MtdError = copy(httpStatus = UNAUTHORIZED)
}

object InvalidBearerTokenError extends MtdError("UNAUTHORIZED", "Bearer token is missing or not authorized", UNAUTHORIZED)

// Accept header Errors
object InvalidAcceptHeaderError extends MtdError("ACCEPT_HEADER_INVALID", "The accept header is missing or invalid", NOT_ACCEPTABLE)

object UnsupportedVersionError extends MtdError("NOT_FOUND", "The requested resource could not be found", NOT_FOUND)

// Common rule errors
object RuleRequestCannotBeFulfilledError
    extends MtdError("RULE_REQUEST_CANNOT_BE_FULFILLED", "Custom (will vary in production depending on the actual error)", 422)

object RuleTaxYearNotSupportedError
    extends MtdError("RULE_TAX_YEAR_NOT_SUPPORTED", "The tax year specified does not lie within the supported range", BAD_REQUEST)

object RuleTaxYearForVersionNotSupportedError
    extends MtdError(
      "RULE_TAX_YEAR_FOR_VERSION_NOT_SUPPORTED",
      "This version of the API supports tax years up to, and including, 2024-25. For tax years 2025–26 onwards, a new version is in development",
      BAD_REQUEST)

object RuleIncorrectOrEmptyBodyError
    extends MtdError("RULE_INCORRECT_OR_EMPTY_BODY_SUBMITTED", "An empty or non-matching body was submitted", BAD_REQUEST)

object RuleTaxYearRangeInvalidError extends MtdError("RULE_TAX_YEAR_RANGE_INVALID", "A tax year range of one year is required", BAD_REQUEST)

object RuleTaxYearNotEndedError extends MtdError("RULE_TAX_YEAR_NOT_ENDED", "The specified tax year has not yet ended", BAD_REQUEST)

object RuleDuplicatedPpdSubmissionIdError
    extends MtdError("RULE_DUPLICATED_PPD_SUBMISSION_ID", "A provided ppdSubmissionId is duplicated", BAD_REQUEST) {

  def forDuplicatedIdAndPaths(id: String, paths: Seq[String]): MtdError = copy(
    message = s"The ppdSubmissionId '$id' is duplicated",
    paths = Some(paths)
  )

}

object RuleIncorrectDisposalTypeError
    extends MtdError("RULE_INCORRECT_DISPOSAL_TYPE", "A provided ppdSubmissionId is being used for the incorrect disposal type", BAD_REQUEST)

object RuleGainLossError extends MtdError("RULE_GAIN_LOSS", "Only one of gain or loss values can be provided", BAD_REQUEST)

object RuleIncorrectLossesSubmittedError
    extends MtdError(
      "RULE_INCORRECT_LOSSES_SUBMITTED",
      "LossesFromThisYear can not be submitted if the numberOfDisposals is less than or equal to 1",
      BAD_REQUEST)

object RuleIncorrectNonStandardGainsSubmittedError
    extends MtdError(
      "RULE_INCORRECT_NON_STANDARD_GAINS",
      "At least one of carriedInterestGain, attributedGains or otherGains must be provided",
      BAD_REQUEST)

object RuleDateRangeInvalidError extends MtdError(code = "RULE_DATE_RANGE_INVALID", message = "The specified date range is invalid", BAD_REQUEST)

object RuleDisposalDateError extends MtdError("RULE_DISPOSAL_DATE", "The disposalDate must be within the specified tax year", BAD_REQUEST)

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
    extends MtdError("RULE_AMOUNT_GAIN_LOSS", "Either amountOfNetGain or amountOfNetLoss, must be provided but not both", BAD_REQUEST)

object RuleInvalidClaimDisposalsError
    extends MtdError(
      "RULE_INVALID_CLAIM_DISPOSALS",
      "BAD and INV relief codes cannot be claimed in the same disposal. Itemise disposals separately",
      BAD_REQUEST)

object RuleMissingCompanyNameError extends MtdError("RULE_MISSING_COMPANY_NAME", "companyName is required for listed shares", BAD_REQUEST)

object RuleInvalidClaimOrElectionCodesError
    extends MtdError("RULE_INVALID_CLAIM_OR_ELECTION_CODES", "INV relief code does not apply to non-UK residential property disposals", BAD_REQUEST) {

  def forListedShares: MtdError = copy(message = "PRR and LET relief codes do not apply to listed shares disposals")
}

object RuleOutsideAmendmentWindowError extends MtdError("RULE_OUTSIDE_AMENDMENT_WINDOW", "You are outside the amendment window", BAD_REQUEST)

// Not found errors
object PpdSubmissionIdNotFoundError extends MtdError("PPD_SUBMISSION_ID_NOT_FOUND", "Matching resource not found", NOT_FOUND)

//Stub Errors
object RuleIncorrectGovTestScenarioError
    extends MtdError("RULE_INCORRECT_GOV_TEST_SCENARIO", "The supplied Gov-Test-Scenario is not valid", BAD_REQUEST)
