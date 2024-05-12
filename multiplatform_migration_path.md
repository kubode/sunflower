# Make your Android app to Multiplatform app
https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-integrate-in-existing-app.html

## Migration path
- [x] Migrate XML views to Compose
- [x] Migrate non-KMP libraries to KMP
    - [x] Migrate Gson to kotlinx.serialization
    - [x] Migrate Retrofit to Ktor
    - [x] Migrate Glide to Coil
    - [x] Migrate Dagger to Koin
- [ ] Update AndroidX libraries to KMP supported versions
- [ ] Migrate `res` files with Multiplatform Resources
- [ ] Rename `:app` to `:androidApp`
- [ ] Create `:shared` module
- [ ] Move sharable code to `commonMain` in `:shared`
- [ ] Create `:desktopApp` module
- [ ] Create `:iosApp` module
