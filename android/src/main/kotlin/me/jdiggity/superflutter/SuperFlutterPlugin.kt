package me.jdiggity.superflutter

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class SuperFlutterPlugin: MethodCallHandler {
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "super_flutter")
      channel.setMethodCallHandler(SuperFlutterPlugin())
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    // Handle method calls in here
    when (call.method) {
      "get_root" -> getRootPermissions(result)
    }
  }

  private fun getRootPermissions(result: Result) {
    var p: Process
    try {
      p = Runtime.getRuntime().exec("su")
      try {
        p.waitFor()
        if (p.exitValue() != 255) {
          result.success("Got root permissions")
        } else {
          result.error("Could not obtain root permissions")
        }
      } catch (InterruptedException e) {
        result.error("Could not obtain root permissions")
      }
    } catch (IOException e) {
      result.error("There was an IO Exception")
    }
  }
}
