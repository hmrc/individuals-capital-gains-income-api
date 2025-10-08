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

package v3.residentialPropertyDisposals.retrieveCgtPpdOverrides.def1

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.implicits.*
import common.errors.SourceFormatError
import shared.controllers.validators.Validator
import shared.controllers.validators.resolvers.ResolveNino
import shared.models.domain.TaxYear
import shared.models.errors.MtdError
import v3.residentialPropertyDisposals.retrieveCgtPpdOverrides.def1.model.request.Def1_RetrieveCgtPpdOverridesRequestData
import v3.residentialPropertyDisposals.retrieveCgtPpdOverrides.model.MtdSourceEnum
import v3.residentialPropertyDisposals.retrieveCgtPpdOverrides.model.request.RetrieveCgtPpdOverridesRequestData

import javax.inject.Inject
import scala.util.{Failure, Success, Try}

class Def1_RetrieveCgtPpdOverridesValidator @Inject() (nino: String, taxYear: String, source: Option[String])
    extends Validator[RetrieveCgtPpdOverridesRequestData] {

  def validate: Validated[Seq[MtdError], RetrieveCgtPpdOverridesRequestData] =
    (
      ResolveNino(nino),
      resolveMtdSource(source)
    ).mapN((validNino, validSource) => Def1_RetrieveCgtPpdOverridesRequestData(validNino, TaxYear.fromMtd(taxYear), validSource))

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
