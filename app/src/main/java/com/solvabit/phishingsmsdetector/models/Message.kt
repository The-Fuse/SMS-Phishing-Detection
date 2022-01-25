package com.solvabit.phishingsmsdetector.models

data class Message(
    var _id: Int = -1,
    var address: String = "",
    var person: String? = "",    // ID of the sender of the conversation, if present
    var date: Long = -1,         // date the message was received
    var date_sent: Long = -1,    // date the message was sent
    var read: Int = -1,          // Has the message been read?
    var seen: Int = -1,          // Has the message been seen by the user? The "seen" flag determines whether we need to show a notification
    var replyPathPresent: Int = -1,
    var subject: String? = "",
    var body: String = "",
    var serviceCenter: String? = "",
    var creator: String? = "",
    var type: Int = -1
)

object SMS {
    const val COLUMN_ID = "_id"
    const val COLUMN_THREAD_ID = "thread_id"
    const val COLUMN_ADDRESS = "address"
    const val COLUMN_PERSON = "person"
    const val COLUMN_DATE = "date"
    const val COLUMN_DATE_SENT = "date_sent"
    const val COLUMN_PROTOCOL = "protocol"
    const val COLUMN_READ = "read"
    const val COLUMN_STATUS = "status"
    const val COLUMN_TYPE = "type"
    const val COLUMN_REPLY_PATH_PRESENT = "reply_path_present"
    const val COLUMN_SUBJECT = "subject"
    const val COLUMN_BODY = "body"
    const val COLUMN_SERVICE_CENTER = "service_center"
    const val COLUMN_LOCKED = "locked"
    const val COLUMN_ERROR_CODE = "error_code"
    const val COLUMN_SEEN = "seen"
    const val COLUMN_TIMED = "timed"
    const val COLUMN_DELETED = "deleted"
    const val COLUMN_SYNC_STATE = "sync_state"
    const val COLUMN_MARKER = "marker"
    const val COLUMN_SOURCE = "source"
    const val COLUMN_BIND_ID = "bind_id"
    const val COLUMN_MX_STATUS = "mx_status"
    const val COLUMN_MX_ID = "mx_id"
    const val COLUMN_OUT_TIME = "out_time"
    const val COLUMN_ACCOUNT = "account"
    const val COLUMN_SIM_ID = "sim_id"
    const val COLUMN_BLOCK_TYPE = "block_type"
    const val COLUMN_ADVANCED_SEEN = "advanced_seen"
    const val COLUMN_B2C_TTL = "b2c_ttl"
    const val COLUMN_B2C_NUMBERS = "b2c_numbers"
    const val COLUMN_FAKE_CELL_TYPE = "fake_cell_type"
    const val COLUMN_URL_RISKY_TYPE = "url_risky_type"
}

