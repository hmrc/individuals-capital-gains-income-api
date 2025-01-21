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

package v2.residentialPropertyDisposals.retrieveAll

import shared.controllers.validators.Validator
import config.CgtAppConfig
import v2.residentialPropertyDisposals.retrieveAll.RetrieveAllResidentialPropertyCgtSchema.Def1
import v2.residentialPropertyDisposals.retrieveAll.def1.Def1_RetrieveAllResidentialPropertyCgtValidator
import v2.residentialPropertyDisposals.retrieveAll.model.request.RetrieveAllResidentialPropertyCgtRequestData

import javax.inject.Inject

class RetrieveAllResidentialPropertyCgtValidatorFactory @Inject() (appConfig: CgtAppConfig) {

  def validator(nino: String, taxYear: String, source: Option[String]): Validator[RetrieveAllResidentialPropertyCgtRequestData] = {
    val schema = RetrieveAllResidentialPropertyCgtSchema.schema

    schema match {
      case Def1 => new Def1_RetrieveAllResidentialPropertyCgtValidator(nino, taxYear, source)(appConfig)
    }

  }

}
