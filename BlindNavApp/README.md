# 시각 장애인을 위한 음성 기반 길 안내 앱 (BlindNav)

## 📌 프로젝트 소개
이 프로젝트는 시각 장애인이 스마트폰 화면을 보지 않고도 목적지를 설정하고 길 안내를 받을 수 있도록 돕는 안드로이드 애플리케이션입니다. 
복잡한 터치 대신 **전체 화면 롱프레스(Long Press)** 제스처와 **음성 인식(STT)/음성 안내(TTS)**를 핵심 인터페이스로 사용합니다.

## ✨ 주요 기능

### 1. 직관적인 제스처 인터페이스
- **화면 어디든 길게 누르기 (Long Press):** 앱의 어느 화면에서든 화면을 길게 누르면 음성 인식이 시작됩니다. 정밀한 버튼 터치가 필요 없어 시각 장애인에게 최적화되어 있습니다.
- **햅틱 피드백 (Haptic Feedback):** 터치, 음성 인식 대기, 성공/실패 상태를 진동 패턴(틱, 더블 진동, 긴 진동)으로 사용자에게 물리적으로 전달합니다.

### 2. 고대비(High Contrast) UI 디자인
- **검은색 배경(#000000) + 네온 노랑(#FFFF00) 텍스트:** 저시력 사용자가 가장 식별하기 쉬운 색상 조합을 적용했습니다.
- **큰 폰트 및 아이콘:** 핵심 정보를 화면 중앙에 크게 배치하여 가독성을 극대화했습니다.

### 3. 음성 안내 시스템
- **TTS (Text-to-Speech):** 현재 상태, 목적지 정보, 남은 거리 등을 음성으로 안내합니다.
- **STT (Speech-to-Text):** "네비게이션", "보행", "메인" 등의 명령어를 음성으로 인식하여 화면을 전환합니다.

## 🛠 기술 스펙
- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel), Single Activity
- **Accessibility APIs:** 
  - `SpeechRecognizer`: 음성 명령 인식
  - `TextToSpeech`: 상황별 음성 안내
  - `Vibrator / VibratorManager`: 햅틱 피드백 구현

## 📱 화면 구성
1.  **메인 화면 (Main Screen):** 앱 실행 시 진입. 대기 상태.
2.  **네비게이션 화면 (Navigation Screen):** 목적지가 설정된 상태.
3.  **보행 화면 (Walking Screen):** 이동 중 안내 모드.

## 🚀 설치 및 실행 방법
1.  Android Studio에서 프로젝트를 엽니다.
2.  실제 안드로이드 기기 또는 에뮬레이터에 빌드합니다.
3.  앱 최초 실행 시 **마이크(Audio)** 및 **위치(Location)** 권한을 허용해야 정상 작동합니다.

---
*Developed for accessibility and inclusive design.*
