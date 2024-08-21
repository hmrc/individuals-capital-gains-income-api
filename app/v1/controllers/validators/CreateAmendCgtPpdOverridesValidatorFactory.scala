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

import api.controllers.validators.Validator
import api.controllers.validators.resolvers.{ResolveNino, ResolveNonEmptyJsonObject, ResolveTaxYearMinimum}
import api.models.domain.TaxYear
import api.models.errors.MtdError
import cats.data.Validated
import cats.implicits._
import config.AppConfig
import play.api.libs.json.JsValue
import v1.controllers.validators.CreateAmendCgtPpdOverridesRulesValidator.validateBusinessRules
import v1.models.request.createAmendCgtPpdOverrides.{CreateAmendCgtPpdOverridesRequestBody, CreateAmendCgtPpdOverridesRequestData}

import javax.inject.Inject

class CreateAmendCgtPpdOverridesValidatorFactory @Inject() (appConfig: AppConfig) {

  private lazy val minimumTaxYear = appConfig.minimumPermittedTaxYear
  private lazy val resolveTaxYear = ResolveTaxYearMinimum(TaxYear.fromDownstreamInt(minimumTaxYear))
  private val resolveJson         = new ResolveNonEmptyJsonObject[CreateAmendCgtPpdOverridesRequestBody]()

  def validator(nino: String, taxYear: String, body: JsValue): Validator[CreateAmendCgtPpdOverridesRequestData] =
    new Validator[CreateAmendCgtPpdOverridesRequestData] {

      def validate: Validated[Seq[MtdError], CreateAmendCgtPpdOverridesRequestData] =
        (
          ResolveNino(nino),
          resolveTaxYear(taxYear),
          resolveJson(body)
        ).mapN(CreateAmendCgtPpdOverridesRequestData) andThen validateBusinessRules

    }

}
