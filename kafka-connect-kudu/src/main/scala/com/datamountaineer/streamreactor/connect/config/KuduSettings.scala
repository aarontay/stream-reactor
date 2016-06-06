/**
  * Copyright 2015 Datamountaineer.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  **/

package com.datamountaineer.streamreactor.connect.config

import com.datamountaineer.connector.config.Config
import com.datamountaineer.streamreactor.connect.errors.{ErrorPolicy, ErrorPolicyEnum, ThrowErrorPolicy}
import org.apache.kafka.common.config.{AbstractConfig, ConfigException}

/**
  * Created by andrew@datamountaineer.com on 13/05/16. 
  * stream-reactor-maven
  */
case class KuduSetting(routes: List[Config],
                       allowAutoCreate: Map[String, Boolean],
                       allowAutoEvolve: Map[String, Boolean],
                       errorPolicy: ErrorPolicy = new ThrowErrorPolicy,
                       maxRetries: Int = KuduSinkConfig.NBR_OF_RETIRES_DEFAULT,
                       batchSize: Int = KuduSinkConfig.BATCH_SIZE_DEFAULT,
                       schemaRegistryUrl: String
                      )

object KuduSettings {
  def apply(config: AbstractConfig, assigned: List[String], sinkTask: Boolean) : KuduSetting = {

    val raw = config.getString(KuduSinkConfig.EXPORT_ROUTE_QUERY)
    require((raw != null && !raw.isEmpty),  s"No ${KuduSinkConfig.EXPORT_ROUTE_QUERY} provided!")

    //parse query
    val routes: Set[Config] = raw.split(";").map(r => Config.parse(r)).toSet.filter(f=>assigned.contains(f.getSource))

    if (routes.size == 0) {
      throw new ConfigException(s"No routes for for assigned topics in "
        + s"${KuduSinkConfig.EXPORT_ROUTE_QUERY}")
    }

    val errorPolicyE = ErrorPolicyEnum.withName(config.getString(KuduSinkConfig.ERROR_POLICY).toUpperCase)
    val errorPolicy = ErrorPolicy(errorPolicyE)
    val maxRetries = config.getInt(KuduSinkConfig.NBR_OF_RETRIES)
    val batchSize = config.getInt(KuduSinkConfig.BATCH_SIZE)

    val autoCreate = routes.map(r=>(r.getSource, r.isAutoCreate)).toMap
    val autoEvolve = routes.map(r=>(r.getSource, r.isAutoEvolve)).toMap

    val schemaRegUrl = config.getString(KuduSinkConfig.SCHEMA_REGISTRY_URL)

    KuduSetting(routes = routes.toList,
                allowAutoCreate = autoCreate,
                allowAutoEvolve = autoEvolve,
                errorPolicy = errorPolicy,
                maxRetries = maxRetries,
                batchSize = batchSize,
                schemaRegistryUrl = schemaRegUrl)
  }
}


