package com.example.project2_cse535

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class BluetoothService(val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private val appUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Common UUID for SPP

    // Callback for device discovery
    var onDeviceFound: ((BluetoothDevice) -> Unit)? = null

    // Define a BroadcastReceiver for Bluetooth device discovery
    private val discoveryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (BluetoothDevice.ACTION_FOUND == intent.action) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    onDeviceFound?.invoke(it)
                }
            }
        }
    }

    // Start device discovery with permission check
    fun startDiscovery() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BluetoothService", "Bluetooth scan permission not granted")
            return
        }
        bluetoothAdapter?.startDiscovery()
        Log.d("BluetoothService", "Started Bluetooth discovery")

        // Register the BroadcastReceiver for ACTION_FOUND
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(discoveryReceiver, filter)
    }

    // Stop device discovery and unregister the receiver
    fun stopDiscovery() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BluetoothService", "Bluetooth scan permission not granted")
            return
        }
        bluetoothAdapter?.cancelDiscovery()
        Log.d("BluetoothService", "Stopped Bluetooth discovery")

        // Unregister the BroadcastReceiver
        context.unregisterReceiver(discoveryReceiver)
    }

    // Connect to a selected device
    suspend fun connectToDevice(device: BluetoothDevice): Boolean = withContext(Dispatchers.IO) {
        stopDiscovery()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BluetoothService", "Bluetooth connect permission not granted")
            return@withContext false
        }
        bluetoothSocket = device.createRfcommSocketToServiceRecord(appUUID)
        return@withContext try {
            bluetoothSocket?.connect()
            Log.d("BluetoothService", "Connected to device ${device.name}")
            true
        } catch (e: IOException) {
            Log.e("BluetoothService", "Failed to connect", e)
            false
        }
    }

    // Disconnect from the current device
    fun disconnect() {
        try {
            bluetoothSocket?.close()
            bluetoothSocket = null
            Log.d("BluetoothService", "Bluetooth connection closed")
        } catch (e: IOException) {
            Log.e("BluetoothService", "Error closing Bluetooth connection", e)
        }
    }

    // Utility to check if Bluetooth is supported and enabled
    fun isBluetoothAvailable(): Boolean {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled
    }

    // Launch intent to enable Bluetooth if disabled
    fun enableBluetooth() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            // Only start the intent if the context is an Activity and permission is granted
            if (context is android.app.Activity) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("BluetoothService", "Bluetooth connect permission not granted")
                    return
                }
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                context.startActivity(enableBtIntent)
            } else {
                Log.e("BluetoothService", "Context is not an Activity, cannot start enable Bluetooth intent")
            }
        }
    }
}
