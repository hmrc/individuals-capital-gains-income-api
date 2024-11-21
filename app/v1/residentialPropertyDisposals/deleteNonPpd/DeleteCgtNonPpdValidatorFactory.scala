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

package v1.residentialPropertyDisposals.deleteNonPpd

import api.controllers.validators.Validator
import config.AppConfig
import v1.residentialPropertyDisposals.deleteNonPpd.def1.Def1_DeleteCgtNonPpdValidator
import v1.residentialPropertyDisposals.deleteNonPpd.model.request.DeleteCgtNonPpdRequestData
import v1.residentialPropertyDisposals.deleteNonPpd.DeleteCgtNonPpdSchema.Def1

import javax.inject.Inject

class DeleteCgtNonPpdValidatorFactory @Inject() (appConfig: AppConfig) {

//  private lazy val minimumTaxYear = appConfig.minimumPermittedTaxYear
//  private lazy val resolveTaxYear = ResolveTaxYearMinimum(TaxYear.fromDownstreamInt(minimumTaxYear))

  def validator(nino: String, taxYear: String): Validator[DeleteCgtNonPpdRequestData] = {
//    new Validator[DeleteCgtNonPpdRequestData] {
//
//      def validate: Validated[Seq[MtdError], DeleteCgtNonPpdRequestData] =
//        (
//          ResolveNino(nino),
//          resolveTaxYear(taxYear)
//        ).mapN(DeleteCgtNonPpdRequestData)

    val schema = DeleteCgtNonPpdSchema.schema

    schema match {
      case Def1 => new Def1_DeleteCgtNonPpdValidator(nino, taxYear)(appConfig)
    }
    }

}