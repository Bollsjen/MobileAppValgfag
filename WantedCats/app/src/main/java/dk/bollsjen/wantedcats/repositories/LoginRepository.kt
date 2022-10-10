package dk.bollsjen.wantedcats.repositories

import dk.bollsjen.wantedcats.models.LoginInfo
import java.util.ArrayList

object Singleton : LoginRepository(){
}

open class LoginRepository {

    var users: ArrayList<LoginInfo> = ArrayList<LoginInfo>()
    var loginToken: LoginInfo = LoginInfo(0,"","")

    fun getUserByInfo(info: LoginInfo) : LoginInfo{
        for(user in users){
            if(info.email == user.email && user.password == info.password){
                return user
            }
        }

        return info
    }
}