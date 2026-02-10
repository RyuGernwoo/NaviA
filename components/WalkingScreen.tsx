import { Footprints, Shield } from "lucide-react";
import { motion } from "motion/react";
import { useState, useEffect } from "react";

export function WalkingScreen() {
  const [stepCount, setStepCount] = useState(0);

  // 걸음 수 증가 시뮬레이션
  useEffect(() => {
    const interval = setInterval(() => {
      setStepCount((prev) => prev + 1);
    }, 1500);

    return () => clearInterval(interval);
  }, []);

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
          transition={{ repeat: Infinity, duration: 2, ease: "easeInOut" }}
          className="absolute inset-0 -m-16 bg-purple-500 rounded-full opacity-30 border-4 border-purple-400"
        />
        
        {/* 보행 아이콘 */}
        <div className="relative bg-white rounded-full p-12 border-8 border-purple-400">
          <motion.div
            animate={{ x: [-5, 5, -5] }}
            transition={{ repeat: Infinity, duration: 1.5, ease: "easeInOut" }}
          >
            <Footprints className="w-24 h-24 text-black" strokeWidth={3} />
          </motion.div>
        </div>

        {/* 안전 보호 아이콘 */}
        <motion.div
          animate={{ scale: [1, 1.1, 1] }}
          transition={{ repeat: Infinity, duration: 2, ease: "easeInOut", delay: 0.5 }}
          className="absolute -top-6 -right-6 bg-yellow-400 rounded-full p-3 border-4 border-yellow-300"
        >
          <Shield className="w-8 h-8 text-black" strokeWidth={3} />
        </motion.div>
      </motion.div>

      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.3 }}
        className="mt-16 text-center"
      >
        <h1 className="text-4xl font-bold text-white mb-4">
          보행 안전 모드
        </h1>
        <p className="text-white text-xl font-bold leading-relaxed max-w-md mb-8">
          주변 장애물과 위험 요소를
          <br />
          실시간으로 감지하여
          <br />
          안전한 보행을 도와드립니다
        </p>

        {/* 걸음 수 카운터 */}
        <div className="bg-black rounded-3xl p-8 border-4 border-purple-400 max-w-md">
          <div className="text-yellow-400 text-lg font-bold mb-3">현재 걸음 수</div>
          <motion.div
            key={stepCount}
            initial={{ scale: 1.1 }}
            animate={{ scale: 1 }}
            className="text-7xl font-bold text-purple-400 mb-2"
          >
            {stepCount}
          </motion.div>
          <div className="text-white text-2xl font-bold">걸음</div>
        </div>
      </motion.div>

      {/* 상태 표시 */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.6 }}
        className="mt-12 space-y-4 w-full max-w-md"
      >
        <div className="bg-black rounded-2xl p-4 flex items-center gap-4 border-4 border-green-400">
          <div className="w-4 h-4 bg-green-400 rounded-full animate-pulse" />
          <div className="flex-1">
            <div className="text-green-400 font-bold text-lg">주변 환경 감지 활성화</div>
            <div className="text-white font-bold">안전한 경로 안내 중</div>
          </div>
        </div>

        <div className="bg-black rounded-2xl p-4 flex items-center gap-4 border-4 border-blue-400">
          <div className="w-4 h-4 bg-blue-400 rounded-full animate-pulse" />
          <div className="flex-1">
            <div className="text-blue-400 font-bold text-lg">음성 안내 활성화</div>
            <div className="text-white font-bold">실시간 정보 제공 중</div>
          </div>
        </div>
      </motion.div>
    </div>
  );
}