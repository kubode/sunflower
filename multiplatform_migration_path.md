# Make your Android app to Multiplatform app
https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-integrate-in-existing-app.html

## Migration path
- [x] Migrate XML views to Compose
- [x] Migrate non-KMP libraries to KMP
    - [x] Migrate Gson to kotlinx.serialization 373fac5871f5af5047c30a6b8e2ba0f71993f101...8e6b5b12cca6fd8b994b03b5f7412e5bfcbe76b1
    - [x] Migrate Retrofit to Ktor 8e6b5b12cca6fd8b994b03b5f7412e5bfcbe76b1...00f117a49dcda6b8cae774102977fdd6eeeae921
    - [x] Migrate Glide to Coil 718000103a8f665339f4898a62c331229304947d...ba6f923f0687830b344545c95a314a5a4b4e0056
    - [x] Migrate Dagger to Koin edf3b1da66016bc0c59a17a157b5a6b24fb29831...15ddae1bf7b7cf80f0d27d3332b6187ade174a94
- [x] Update AndroidX libraries to KMP supported versions 87dfcc3806853328b91f8375b17e38b46ab83888
- [x] Migrate `:app` to Multiplatform project f890cce01644a25e6b31aefa26324ae3cc4c9993
    - [x] Apply `kotlin-multiplatform` plugin with fixing the dependencies
    - [x] Rename all `java` directories to `kotlin`
- [x] Migrate `:app` to use Compose Multiplatform d725d1b095a5886e298d95bdc5077008bc630edf
    - [x] Migrate DataBinding to ViewBinding cccc98e043efaca982b448f2646e6b68ec620b47
    - [x] Migrate LiveData to Flow c63ec96c577f53df12ffd2ae35493f15fde33fe1
- [x] Migrate `res` files with Multiplatform Resources 1333850dfaa5fd438d329a32182c40a9ef25c827...9aa0101e2dcdce2f358f025d05f893a4e75b3deb
    - [Limitations](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-images-resources.html)
- [ ] Rename `:app` to `:androidApp`
- [ ] Create `:shared` module
- [ ] Move sharable code to `commonMain` in `:shared`
- [ ] Create `:desktopApp` module
- [ ] Create `:iosApp` module
