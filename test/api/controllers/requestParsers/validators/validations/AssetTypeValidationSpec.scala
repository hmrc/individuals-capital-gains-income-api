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

package api.controllers.requestParsers.validators.validations

import api.models.errors.AssetTypeFormatError
import support.UnitSpec

class AssetTypeValidationSpec extends UnitSpec {

  "AssetTypeValidation" when {
    "validate" must {
      "return an empty list for a valid scheme plan type" in {
        AssetTypeValidation.validate(assetType = "other-property") shouldBe NoValidationErrors
        AssetTypeValidation.validate(assetType = "unlisted-shares") shouldBe NoValidationErrors
        AssetTypeValidation.validate(assetType = "listed-shares") shouldBe NoValidationErrors
        AssetTypeValidation.validate(assetType = "other-asset") shouldBe NoValidationErrors
      }

      "return a AssetTypeFormatError for a invalid scheme plan type" in {
        AssetTypeValidation.validate(assetType = "Not an asset type") shouldBe List(AssetTypeFormatError)
      }
    }
  }

}
