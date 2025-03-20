#!/bin/bash

# Android SDK ve gerekli araçları otomatik kuran script

# 1. Gerekli paketleri yükle
echo "Gerekli bağımlılıklar yükleniyor..."
sudo apt update && sudo apt install -y openjdk-17-jdk unzip wget

# 2. Android SDK dizinini oluştur
ANDROID_HOME=$HOME/Android/Sdk
mkdir -p "$ANDROID_HOME"

# 3. Android Komut Satırı Araçlarını indir
echo "Android SDK Komut Satırı Araçları indiriliyor..."
cd "$ANDROID_HOME" || exit
wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O cmdline-tools.zip

# 4. Dosyayı çıkart ve doğru dizine taşı
echo "Dosyalar çıkartılıyor..."
unzip -q cmdline-tools.zip
mkdir -p cmdline-tools/latest
mv cmdline-tools/* cmdline-tools/latest/
rm cmdline-tools.zip

# 5. Ortam değişkenlerini ayarla
echo "Ortam değişkenleri ekleniyor..."
PROFILE_FILE="$HOME/.bashrc"
if [ -n "$ZSH_VERSION" ]; then
  PROFILE_FILE="$HOME/.zshrc"
fi

{
  echo 'export ANDROID_HOME=$HOME/Android/Sdk'
  echo 'export ANDROID_SDK_ROOT=$ANDROID_HOME'
  echo 'export PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH'
  echo 'export PATH=$ANDROID_HOME/tools/bin:$PATH'
  echo 'export PATH=$ANDROID_HOME/cmdline-tools/bin:$PATH'
} >> "$PROFILE_FILE"

source "$PROFILE_FILE"

# 6. Android SDK bileşenlerini yükle
echo "Android SDK bileşenleri yükleniyor..."
yes | sdkmanager --licenses
sdkmanager --update
sdkmanager --install "platform-tools" "platforms;android-34" "build-tools;34.0.0"

echo "✅ Android SDK başarıyla kuruldu!"
echo "🔄 Değişikliklerin etkin olması için terminali kapatıp aç veya 'source $PROFILE_FILE' komutunu çalıştır."
