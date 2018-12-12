package me.leoyuu.opengl.vr


fun perspectiveM(
    m: FloatArray, offset: Int,
    fovy: Float, aspect: Float, zNear: Float, zFar: Float
) {
    val f = 1.0f / Math.tan(fovy * (Math.PI / 360.0)).toFloat()
    val rangeReciprocal = 1.0f / (zNear - zFar)

    m[offset + 0] = f / aspect
    m[offset + 1] = 0.0f
    m[offset + 2] = 0.0f
    m[offset + 3] = 0.0f

    m[offset + 4] = 0.0f
    m[offset + 5] = f
    m[offset + 6] = 0.0f
    m[offset + 7] = 0.0f

    m[offset + 8] = 0.0f
    m[offset + 9] = 0.0f
    m[offset + 10] = (zFar + zNear) * rangeReciprocal
    m[offset + 11] = -1.0f

    m[offset + 12] = 0.0f
    m[offset + 13] = 0.0f
    m[offset + 14] = 2.0f * zFar * zNear * rangeReciprocal
    m[offset + 15] = 0.0f
}