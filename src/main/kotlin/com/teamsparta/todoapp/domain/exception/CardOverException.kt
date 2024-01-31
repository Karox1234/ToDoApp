package com.teamsparta.todoapp.domain.exception

import java.io.Serial

class CardOverException : RuntimeException {


    @Serial
    private val serialVersionUID: Long = -4937536120992271478L

    //기본 생성자
    constructor() : super()

    //메시지를 포함하는 생성자
    constructor(message: String) : super(message)

    //메시지와 원인을 포함하는 생성자
    constructor(message: String, cause: Throwable) : super(message, cause)

    //원인만 포함하는 생성자
    constructor(cause: Throwable) : super(cause)

    //커스텀 생성자
    constructor(
        message: String,
        cause: Throwable,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(message, cause, enableSuppression, writableStackTrace)
}

//이런식으로 적용 범위가 너무 좁은 커스텀 exception은 지양하는게 좋으나,
//아직 코드가 많이 확장된게 아니라 사용할수 있는게 너무 제한적이라 연습용으로 생성 하였습니다.
//이 exception은 IllegalArgumentException으로 대체 가능할것 같습니다.