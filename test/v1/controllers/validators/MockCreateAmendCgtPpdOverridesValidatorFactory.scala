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
import play.api.libs.json.JsValue
import v1.residentialPropertyDisposals.createAmendCgtPpdOverrides.CreateAmendCgtPpdOverridesValidatorFactory
import v1.residentialPropertyDisposals.createAmendCgtPpdOverrides.def1.model.request.Def1_CreateAmendCgtPpdOverridesRequestData

trait MockCreateAmendCgtPpdOverridesValidatorFactory extends MockFactory {

  val mockCreateAmendCgtPpdOverridesValidatorFactory: CreateAmendCgtPpdOverridesValidatorFactory =
    mock[CreateAmendCgtPpdOverridesValidatorFactory]

  object MockedCreateAmendCgtPpdOverridesValidatorFactory {

    def validator(): CallHandler[Validator[Def1_CreateAmendCgtPpdOverridesRequestData]] =
      (mockCreateAmendCgtPpdOverridesValidatorFactory.validator(_: String, _: String, _: JsValue)).expects(*, *, *)

  }

  def willUseValidator(use: Validator[Def1_CreateAmendCgtPpdOverridesRequestData]): CallHandler[Validator[Def1_CreateAmendCgtPpdOverridesRequestData]] = {
    MockedCreateAmendCgtPpdOverridesValidatorFactory
      .validator()
      .anyNumberOfTimes()
      .returns(use)
  }

  def returningSuccess(result: Def1_CreateAmendCgtPpdOverridesRequestData): Validator[Def1_CreateAmendCgtPpdOverridesRequestData] =
    new Validator[Def1_CreateAmendCgtPpdOverridesRequestData] {
      def validate: Validated[Seq[MtdError], Def1_CreateAmendCgtPpdOverridesRequestData] = Valid(result)
    }

  def returning(result: MtdError*): Validator[Def1_CreateAmendCgtPpdOverridesRequestData] = returningErrors(result)

  def returningErrors(result: Seq[MtdError]): Validator[Def1_CreateAmendCgtPpdOverridesRequestData] =
    new Validator[Def1_CreateAmendCgtPpdOverridesRequestData] {
      def validate: Validated[Seq[MtdError], Def1_CreateAmendCgtPpdOverridesRequestData] = Invalid(result)
    }

}
