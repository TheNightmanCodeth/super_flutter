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
      "run_cmd" -> runCommand(result, call.argument("cmd"))
    }
  }

  /** 
      Only requests root from superuser app to be added to whitelist 
      This method should really never be used, since some users will
      leave the whitelist feature of their root app of choice off.
  */
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

  private fun runCommand(result: Result, cmd: String) {
    var p: Process
    try {
      p = Runtime.getRuntime().exec("su")
      val os: DataOutputStream = DataOutputStream(p.getOutputStream())
      os.writeBytes("$cmd\n")
      os.writeBytes("exit\n")
      os.flush()
      try {
        p.waitFor()
        if (p.exitValue() != 255) {
          result.success("Process exited successfully")
        } else {
          result.error(p.exitValue().toString())
        }
      } catch (InterruptedException e) {
        result.error(e.message)
      }
    } catch (IOException e) {
      result.error(e.message)
    }
  }
}
