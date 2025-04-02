package com.example.contentproviders

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var listViewContacts: ListView  // ListView để hiển thị danh sách liên hệ
    private lateinit var contactsList: ArrayList<String> // Danh sách chua dữ liệu liên hệ
    private lateinit var contactsAdapter: ArrayAdapter<String> // Adapter để hiển thị dữ liệu lên ListView

    private val REQUEST_READ_CONTACTS_PERMISSION = 100 // Request code để xin quyền READ_CONTACTS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewContacts = findViewById(R.id.listViewContacts)
        contactsList = ArrayList()

        // Kiểm tra và xin quyền READ_CONTACTS truoc khi truy cập danh bạ
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Neu chua co quyen thi xin nguoi dung cap quyen
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS_PERMISSION
            )
        } else {
            // Neu da co quyen thi truy cap danh ba
            loadContacts()
        }
    }
    // Xử lý kết quả xin quyền READ_CONTACTS
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_CONTACTS_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu người dùng cấp quyền, truy cập danh bạ
                loadContacts()
            } else {
                // Nếu người dùng không cấp quyền, thông báo
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //  Hàm truy cập danh bạ và hiển thị lên ListView ( Lấy dữ liệu )
    private fun loadContacts() {
        val contactMap = LinkedHashMap<String, String>() // Lưu danh bạ (tên -> thông tin)

        // Truy vấn danh sách tên liên hệ
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, // URI của danh bạ
            null, // Lấy tất cả các cột
            null, // Không có điều kiện lọc
            null,
            null
        )

        cursor?.use {
            if (it.count > 0) { // Kiểm tra xem có dữ liệu không
                while (it.moveToNext()) {
                    val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID) // Lấy ID của liên hệ
                    val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) // Lấy tên liên hệ

                    val contactId = it.getString(idIndex) // ID liên hệ
                    val contactName = it.getString(nameIndex) // Tên liên hệ

                    val phoneNumber = getPhoneNumber(contactId) // Lấy số điện thoại theo ID
                    val email = getEmail(contactId) // Lấy email theo ID

                    // Tạo thông tin liên hệ
                    val contactInfo = """
                        Tên: $contactName
                        Số điện thoại: ${phoneNumber.ifEmpty { "Không có" }}
                        Email: ${email.ifEmpty { "Không có" }}
                    """.trimIndent()

                    contactMap[contactName] = contactInfo // Lưu vào danh sách
                }
            }
        }

        // Hiển thị danh sách lên ListView
        contactsList.addAll(contactMap.values)
        contactsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactsList)
        listViewContacts.adapter = contactsAdapter
    }

    @SuppressLint("Range")
    private fun getPhoneNumber(contactId: String): String {
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )

        var phoneNumber = ""
        cursor?.use {
            if (it.moveToFirst()) {
                phoneNumber = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }
        return phoneNumber
    }

    @SuppressLint("Range")
    private fun getEmail(contactId: String): String {
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
            "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )

        var email = ""
        cursor?.use {
            if (it.moveToFirst()) {
                email = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
            }
        }
        return email
    }
}
