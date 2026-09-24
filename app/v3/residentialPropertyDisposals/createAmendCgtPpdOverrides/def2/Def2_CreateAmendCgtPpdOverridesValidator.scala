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

package v3.residentialPropertyDisposals.createAmendCgtPpdOverrides.def2

import api.config.AppConfig
import api.controllers.validators.Validator
import api.controllers.validators.resolvers.{ResolveNino, ResolveNonEmptyJsonObject, ResolveTaxYearMinimum}
import api.models.domain.TaxYear
import api.models.errors.MtdError
import cats.data.Validated
import cats.implicits.*
import play.api.libs.json.JsValue
import v3.residentialPropertyDisposals.createAmendCgtPpdOverrides.def2.Def2_CreateAmendCgtPpdOverridesRulesValidator.validateBusinessRules
import v3.residentialPropertyDisposals.createAmendCgtPpdOverrides.def2.model.request.{
  Def2_CreateAmendCgtPpdOverridesRequestBody,
  Def2_CreateAmendCgtPpdOverridesRequestData
}
import v3.residentialPropertyDisposals.createAmendCgtPpdOverrides.model.request.CreateAmendCgtPpdOverridesRequestData

class Def2_CreateAmendCgtPpdOverridesValidator(nino: String, taxYear: String, body: JsValue, temporalValidationEnabled: Boolean)(implicit
    appConfig: AppConfig)
    extends Validator[CreateAmendCgtPpdOverridesRequestData] {
  private val resolveJson = new ResolveNonEmptyJsonObject[Def2_CreateAmendCgtPpdOverridesRequestBody]()

  private val resolveTaxYear =
    ResolveTaxYearMinimum(TaxYear.ending(appConfig.minimumPermittedTaxYear), allowIncompleteTaxYear = !temporalValidationEnabled)

  def validate: Validated[Seq[MtdError], CreateAmendCgtPpdOverridesRequestData] = (
    ResolveNino(nino),
    resolveTaxYear(taxYear),
    resolveJson(body)
  ).mapN(Def2_CreateAmendCgtPpdOverridesRequestData.apply) andThen validateBusinessRules

}
