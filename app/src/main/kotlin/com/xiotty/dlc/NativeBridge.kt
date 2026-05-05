package com.xiotty.dlc

object NativeBridge {
    external fun setESPBox(enabled: Boolean)
    external fun setSnaplines(enabled: Boolean)
    external fun setSilentAim(enabled: Boolean)
    external fun setAimFOV(value: Int)
    external fun setChams(enabled: Boolean)
    external fun setBunnyHop(enabled: Boolean)
    external fun injectLibrary(pid: Int, libPath: String): Boolean
}
