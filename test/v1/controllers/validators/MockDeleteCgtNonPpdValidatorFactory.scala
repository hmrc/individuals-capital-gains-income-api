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
import api.models.errors.MtdError
import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import org.scalamock.handlers.CallHandler
import org.scalamock.scalatest.MockFactory
import v1.ResidentialPropertyDisposals.delete.DeleteCgtNonPpdValidatorFactory
import v1.ResidentialPropertyDisposals.delete.model.request.DeleteCgtNonPpdRequestData

trait MockDeleteCgtNonPpdValidatorFactory extends MockFactory {

  val mockDeleteCgtNonPpdValidatorFactory: DeleteCgtNonPpdValidatorFactory =
    mock[DeleteCgtNonPpdValidatorFactory]

  object MockedDeleteCgtNonPpdValidatorFactory {

    def validator(): CallHandler[Validator[DeleteCgtNonPpdRequestData]] =
      (mockDeleteCgtNonPpdValidatorFactory.validator(_: String, _: String)).expects(*, *)

  }

  def willUseValidator(use: Validator[DeleteCgtNonPpdRequestData]): CallHandler[Validator[DeleteCgtNonPpdRequestData]] = {
    MockedDeleteCgtNonPpdValidatorFactory
      .validator()
      .anyNumberOfTimes()
      .returns(use)
  }

  def returningSuccess(result: DeleteCgtNonPpdRequestData): Validator[DeleteCgtNonPpdRequestData] =
    new Validator[DeleteCgtNonPpdRequestData] {
      def validate: Validated[Seq[MtdError], DeleteCgtNonPpdRequestData] = Valid(result)
    }

  def returning(result: MtdError*): Validator[DeleteCgtNonPpdRequestData] = returningErrors(result)

  def returningErrors(result: Seq[MtdError]): Validator[DeleteCgtNonPpdRequestData] =
    new Validator[DeleteCgtNonPpdRequestData] {
      def validate: Validated[Seq[MtdError], DeleteCgtNonPpdRequestData] = Invalid(result)
    }

}
