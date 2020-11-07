package org.wit.hillfort.models

interface UserStore {
    fun findAll(): List<UserModel>
    fun signup(user:UserModel): UserModel
    fun findByEmail(email: String): UserModel?
    fun update(user: UserModel)
    fun delete(user: UserModel)
}