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

package v1.otherCgt.createAmend

import api.controllers.validators.Validator
import config.AppConfig
import play.api.libs.json.JsValue
import v1.otherCgt.createAmend.CreateAmendOtherCgtSchema.Def1
import v1.otherCgt.createAmend.def1.Def1_CreateAmendOtherCgtValidator
import v1.otherCgt.createAmend.model.request.CreateAmendOtherCgtRequestData

import javax.inject.{Inject, Singleton}

@Singleton
class CreateAmendOtherCgtValidatorFactory @Inject() (appConfig: AppConfig) {

  def validator(nino: String, taxYear: String, body: JsValue): Validator[CreateAmendOtherCgtRequestData] = {
    val schema = CreateAmendOtherCgtSchema.schema
    schema match {
      case Def1 => new Def1_CreateAmendOtherCgtValidator(nino, taxYear, body)(appConfig)
    }
  }

}
