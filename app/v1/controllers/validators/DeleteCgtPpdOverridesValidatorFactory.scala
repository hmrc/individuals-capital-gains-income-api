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
import api.controllers.validators.resolvers.{ResolveNino, ResolveTaxYearMinimum}
import api.models.domain.TaxYear
import api.models.errors.MtdError
import cats.data.Validated
import cats.implicits._
import v1.models.request.deleteCgtPpdOverrides.DeleteCgtPpdOverridesRequest

import javax.inject.Singleton

@Singleton
class DeleteCgtPpdOverridesValidatorFactory {

  private val deleteCgtPpdOverridesMinimumTaxYear = TaxYear.fromMtd("2017-18")
  private val resolveTaxYear                      = ResolveTaxYearMinimum(deleteCgtPpdOverridesMinimumTaxYear)

  def validator(nino: String, taxYear: String): Validator[DeleteCgtPpdOverridesRequest] =
    new Validator[DeleteCgtPpdOverridesRequest] {

      def validate: Validated[Seq[MtdError], DeleteCgtPpdOverridesRequest] =
        (
          ResolveNino(nino),
          resolveTaxYear(taxYear)
        ).mapN(DeleteCgtPpdOverridesRequest)

    }

}
