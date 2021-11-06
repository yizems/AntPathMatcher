package cn.yizems.ant


fun String.toAntMatcher(): AntPathMatcher {
    return AntPathMatcher(this)
}
