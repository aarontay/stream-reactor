/**
  * Copyright 2016 Datamountaineer.
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

package com.datamountaineeer.streamreactor.connect.rethink.config

import java.util

import org.apache.kafka.common.config.ConfigDef.{Importance, Type}
import org.apache.kafka.common.config.{AbstractConfig, ConfigDef}

/**
  * Created by andrew@datamountaineer.com on 24/03/16. 
  * stream-reactor
  */
object ReThinkSinkConfig {
  val RETHINK_HOST = "connect.rethink.host"
  val RETHINK_HOST_DOC = "Rethink server host."
  val RETHINK_HOST_DEFAULT = "localhost"
  val RETHINK_DB = "connect.rethink.db"
  val RETHINK_DB_DOC = "The target database in reThink."
  val AUTO_CREATE_DB = "connect.auto.create.db"
  val AUTO_CREATE_DB_DOC = "Create the database is if doesn't exist"
  val AUTO_CREATE_DB_DEFAULT = false
  val AUTO_CREATE_TBLS = "connect.rethink.auto.create.tables"
  val AUTO_CREATE_TBLS_DOC = "Create the tables per topic they doesn't exist"
  val AUTO_CREATE_TBLS_DEFAULT = false

  val CONFLICT_POLICY_MAP = "connect.rethink.conflict.policy"
  val CONFLICT_POLICY_DOC =
    """
      |The ReThinkDB conflict resolution policy on insert conflicts. Available options are ERROR, REPLACE and UPDATE.
      |ERROR will cause the SinkRecord not to be insert, this is the default.
      |REPLACE will replace completely the existing record with the new SinkRecord.
      |UPDATE will replace any values that have changed.
      |
      |The policy can be set per target table. For example table1:ERROR,table2:REPLACE,table3:UPDATE
    """.stripMargin

  val CONFLICT_ERROR = "ERROR"
  val CONFLICT_REPLACE = "REPLACE"
  val CONFLICT_UPDATE = "UPDATE"

  val EXPORT_ROUTE_QUERY = "connect.hbase.export.route.query"
  val EXPORT_ROUTE_QUERY_DOC = ""

  val ERROR_POLICY = "connect.hbase.error.policy"
  val ERROR_POLICY_DOC = "Specifies the action to be taken if an error occurs while inserting the data.\n" +
    "There are two available options: \n" + "NOOP - the error is swallowed \n" +
    "THROW - the error is allowed to propagate. \n" +
    "RETRY - The exception causes the Connect framework to retry the message. The number of retries is based on \n" +
    "The error will be logged automatically";
  val ERROR_POLICY_DEFAULT = "THROW"

  val ERROR_RETRY_INTERVAL = "connect.hbase.retry.interval"
  val ERROR_RETRY_INTERVAL_DOC = "The time in milliseconds between retries."
  val ERROR_RETRY_INTERVAL_DEFAULT = "60000"
  val NBR_OF_RETRIES = "connect.hbase.max.retires"
  val NBR_OF_RETRIES_DOC = "The maximum number of times to try the write again."
  val NBR_OF_RETIRES_DEFAULT = 20

  val config: ConfigDef = new ConfigDef()
    .define(RETHINK_HOST, Type.STRING, RETHINK_HOST_DEFAULT, Importance.HIGH, RETHINK_HOST_DOC)
    .define(RETHINK_DB, Type.STRING, Importance.HIGH, RETHINK_HOST_DOC)
    .define(AUTO_CREATE_DB, Type.BOOLEAN, AUTO_CREATE_DB_DEFAULT, Importance.MEDIUM, AUTO_CREATE_DB_DOC)
    .define(AUTO_CREATE_TBLS, Type.BOOLEAN, AUTO_CREATE_TBLS_DEFAULT, Importance.MEDIUM, AUTO_CREATE_TBLS_DOC)
    .define(EXPORT_ROUTE_QUERY, Type.STRING, Importance.HIGH, EXPORT_ROUTE_QUERY)
    .define(ERROR_POLICY, Type.STRING, ERROR_POLICY_DEFAULT, Importance.HIGH, ERROR_POLICY_DOC)
    .define(ERROR_RETRY_INTERVAL, Type.INT, ERROR_RETRY_INTERVAL_DEFAULT, Importance.MEDIUM, ERROR_RETRY_INTERVAL_DOC)
    .define(NBR_OF_RETRIES, Type.INT, NBR_OF_RETIRES_DEFAULT, Importance.MEDIUM, NBR_OF_RETRIES_DOC)
    .define(CONFLICT_POLICY_MAP, Type.STRING, CONFLICT_ERROR, Importance.HIGH, CONFLICT_POLICY_DOC)
}

class ReThinkSinkConfig(props: util.Map[String, String])
  extends AbstractConfig(ReThinkSinkConfig.config, props) {
}
