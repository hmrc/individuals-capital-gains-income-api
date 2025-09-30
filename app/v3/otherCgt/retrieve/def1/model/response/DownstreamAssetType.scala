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

package v3.otherCgt.retrieve.def1.model.response

import play.api.libs.json.Format
import shared.utils.enums.Enums

enum DownstreamAssetType(val toMtd: AssetType) {
  case `otherProperty`  extends DownstreamAssetType(AssetType.`other-property`)
  case `unlistedShares` extends DownstreamAssetType(AssetType.`unlisted-shares`)
  case `listedShares`   extends DownstreamAssetType(AssetType.`listed-shares`)
  case `otherAsset`     extends DownstreamAssetType(AssetType.`other-asset`)
}

object DownstreamAssetType {
  given Format[DownstreamAssetType] = Enums.format(values)
}
