import { Mic } from "lucide-react";
import { motion } from "motion/react";

export function MainScreen() {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center px-6 bg-black">
      <motion.div
        initial={{ scale: 0 }}
        animate={{ scale: 1 }}
        transition={{ type: "spring", stiffness: 200, damping: 20 }}
        className="relative"
      >
        {/* 배경 원형 효과 */}
        <motion.div
          animate={{ scale: [1, 1.1, 1] }}
          transition={{ repeat: Infinity, duration: 3, ease: "easeInOut" }}
          className="absolute inset-0 -m-12 bg-blue-500 rounded-full opacity-30 border-4 border-blue-400"
        />
        
        {/* 마이크 아이콘 */}
        <div className="relative bg-white rounded-full p-12 border-8 border-blue-400">
          <Mic className="w-24 h-24 text-black" strokeWidth={3} />
        </div>
      </motion.div>

      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.3 }}
        className="mt-12 text-center"
      >
        <h1 className="text-4xl font-bold text-white mb-4">
          음성 길 안내
        </h1>
        <p className="text-white text-xl font-bold leading-relaxed max-w-md">
          화면을 길게 눌러
          <br />
          음성으로 목적지를 설정하거나
          <br />
          보행 모드를 시작하세요
        </p>
      </motion.div>

      {/* 기능 안내 */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.6 }}
        className="mt-16 grid grid-cols-1 gap-4 w-full max-w-sm"
      >
        <div className="bg-black rounded-2xl p-6 border-4 border-yellow-400">
          <div className="text-yellow-400 font-bold mb-2 text-lg">
            "네비게이션" 명령
          </div>
          <div className="text-white font-bold">
            목적지 안내를 시작합니다
          </div>
        </div>
        
        <div className="bg-black rounded-2xl p-6 border-4 border-yellow-400">
          <div className="text-yellow-400 font-bold mb-2 text-lg">
            "보행 모드" 명령
          </div>
          <div className="text-white font-bold">
            보행 안전 모드를 활성화합니다
          </div>
        </div>
      </motion.div>
    </div>
  );
}