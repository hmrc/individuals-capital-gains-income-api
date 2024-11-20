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

package v1.residentialPropertyDisposals.retreiveAll

import api.controllers.validators.Validator
import api.controllers.validators.resolvers.{ResolveNino, ResolveTaxYearMinimum}
import api.models.domain.{MtdSourceEnum, TaxYear}
import api.models.errors.{MtdError, SourceFormatError}
import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.implicits.catsSyntaxTuple3Semigroupal
import config.AppConfig
import v1.residentialPropertyDisposals.retreiveAll.model.request.RetrieveAllResidentialPropertyCgtRequestData

import javax.inject.Inject
import scala.util.{Failure, Success, Try}

class RetrieveAllResidentialPropertyCgtValidatorFactory @Inject() (appConfig: AppConfig) {

  private lazy val minimumTaxYear = appConfig.minimumPermittedTaxYear
  private lazy val resolveTaxYear = ResolveTaxYearMinimum(TaxYear.fromDownstreamInt(minimumTaxYear))

  def validator(nino: String, taxYear: String, source: Option[String]): Validator[RetrieveAllResidentialPropertyCgtRequestData] =
    new Validator[RetrieveAllResidentialPropertyCgtRequestData] {

      def validate: Validated[Seq[MtdError], RetrieveAllResidentialPropertyCgtRequestData] =
        (
          ResolveNino(nino),
          resolveTaxYear(taxYear),
          resolveMtdSource(source)
        ).mapN(RetrieveAllResidentialPropertyCgtRequestData)

    }

  private def resolveMtdSource(maybeSource: Option[String]): Validated[Seq[MtdError], MtdSourceEnum] = {
    maybeSource
      .map { source =>
        Try {
          MtdSourceEnum.parser(source)
        } match {
          case Success(mtdSource) => Valid(mtdSource)
          case Failure(_)         => Invalid(List(SourceFormatError))
        }
      }
      .getOrElse(Valid(MtdSourceEnum.latest))
  }

}
