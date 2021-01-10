package com.example.recorder.repository

import com.example.recorder.model.UdpRecordFolder
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component

@Component
class UdpRecordFolderRepositoryImpl(
    val repositoryBase: UdpRecordFolderRepositoryBase
): UdpRecordFolderRepository {

    private val defaultFolder = UdpRecordFolder("${System.getProperty("user.dir")}/UdpRecordedData/")

    override fun find(): UdpRecordFolder {
        val folders = repositoryBase.findAll()
        if (folders.isEmpty()) {
            repositoryBase.save(defaultFolder)
            return repositoryBase.findAll().last()
        }
        return folders.last()
    }

    override fun save(udpRecordFolder: UdpRecordFolder): UdpRecordFolder {
        deleteAll()
        repositoryBase.save(udpRecordFolder)
        return find()
    }

    override fun delete(): UdpRecordFolder {
        deleteAll()
        setDefault()
        return find()
    }

    private fun setDefault() = repositoryBase.save(defaultFolder)

    private fun deleteAll() {
        val folders = repositoryBase.findAll()
        folders.forEach {
            repositoryBase.deleteById(it.id!!)
        }
    }
}

interface UdpRecordFolderRepository {

    fun find(): UdpRecordFolder

    fun save(udpRecordFolder: UdpRecordFolder): UdpRecordFolder

    fun delete(): UdpRecordFolder

}

interface UdpRecordFolderRepositoryBase: Repository<UdpRecordFolder, Long> {

    fun save(udpRecordFolder: UdpRecordFolder): UdpRecordFolder

    fun findAll(): List<UdpRecordFolder>

    fun findById(udpRecordAddressId: Long): UdpRecordFolder

    fun deleteById(udpRecordAddressId: Long)

}