package com.hugeinc.sxsw.rest

import com.hugeinc.sxsw.model.UserRequest
import com.hugeinc.sxsw.model.UserResponse
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Api interface with endpoint calls
 *
 * User: cmunoz
 * Date: 1/20/17
 * Time: 12:14 PM
 */
interface ApiInterface {

  @POST("beaconIn")
  fun beaconIn(@Body request: UserRequest): Flowable<UserResponse>
  @POST("beaconOut")
  fun beaconOut(@Body request: UserRequest): Flowable<UserResponse>
}