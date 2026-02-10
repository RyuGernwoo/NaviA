import { Outlet, useNavigate } from "react-router";
import { useState, useRef, useEffect } from "react";
import { Mic } from "lucide-react";
import { motion, AnimatePresence } from "motion/react";

export function Layout() {
  const navigate = useNavigate();
  const [isListening, setIsListening] = useState(false);
  const [recognizedText, setRecognizedText] = useState("");
  const holdTimerRef = useRef<NodeJS.Timeout | null>(null);
  const longPressThreshold = 500; // 0.5초 이상 누르면 음성 인식 시작

  const handleTouchStart = () => {
    holdTimerRef.current = setTimeout(() => {
      startListening();
    }, longPressThreshold);
  };

  const handleTouchEnd = () => {
    if (holdTimerRef.current) {
      clearTimeout(holdTimerRef.current);
    }
    if (isListening) {
      stopListening();
    }
  };

  const startListening = () => {
    setIsListening(true);
    setRecognizedText("");
  };

  const stopListening = () => {
    setIsListening(false);
    // 음성 인식 시뮬레이션 - 실제로는 Kotlin에서 구현
    simulateVoiceRecognition();
  };

  const simulateVoiceRecognition = () => {
    // 데모용 시뮬레이션
    setTimeout(() => {
      const commands = [
        { text: "네비게이션", action: () => navigate("/navigation") },
        { text: "보행 모드", action: () => navigate("/walking") },
        { text: "메인", action: () => navigate("/") },
      ];
      
      const randomCommand = commands[Math.floor(Math.random() * commands.length)];
      setRecognizedText(randomCommand.text);
      
      setTimeout(() => {
        randomCommand.action();
        setRecognizedText("");
      }, 1000);
    }, 500);
  };

  useEffect(() => {
    return () => {
      if (holdTimerRef.current) {
        clearTimeout(holdTimerRef.current);
      }
    };
  }, []);

  return (
    <div 
      className="min-h-screen bg-black relative overflow-hidden"
      onMouseDown={handleTouchStart}
      onMouseUp={handleTouchEnd}
      onTouchStart={handleTouchStart}
      onTouchEnd={handleTouchEnd}
    >
      <Outlet />

      {/* 음성 인식 인디케이터 */}
      <AnimatePresence>
        {isListening && (
          <motion.div
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 0.8 }}
            className="fixed top-8 left-1/2 -translate-x-1/2 z-50"
          >
            <div className="bg-yellow-400 text-black px-6 py-3 rounded-full border-4 border-yellow-300 flex items-center gap-3">
              <motion.div
                animate={{ scale: [1, 1.2, 1] }}
                transition={{ repeat: Infinity, duration: 1 }}
              >
                <Mic className="w-5 h-5" strokeWidth={3} />
              </motion.div>
              <span className="font-bold">음성 인식 중...</span>
            </div>
          </motion.div>
        )}
      </AnimatePresence>

      {/* 인식된 텍스트 표시 */}
      <AnimatePresence>
        {recognizedText && (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            className="fixed top-24 left-1/2 -translate-x-1/2 z-50"
          >
            <div className="bg-green-400 text-black px-6 py-3 rounded-full border-4 border-green-300">
              <span className="font-bold">"{recognizedText}"</span>
            </div>
          </motion.div>
        )}
      </AnimatePresence>

      {/* 하단 안내 문구 */}
      <div className="fixed bottom-8 left-1/2 -translate-x-1/2 z-10">
        <p className="text-white text-lg font-bold text-center px-4 bg-black border-2 border-white py-3 rounded-full">
          화면을 길게 눌러 음성 명령을 시작하세요
        </p>
      </div>
    </div>
  );
}