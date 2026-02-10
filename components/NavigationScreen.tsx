import { MapPin, Navigation } from "lucide-react";
import { motion } from "motion/react";
import { useState, useEffect } from "react";

export function NavigationScreen() {
  const [destination] = useState("서울역");
  const [distance, setDistance] = useState(850);

  // 거리 감소 시뮬레이션
  useEffect(() => {
    const interval = setInterval(() => {
      setDistance((prev) => Math.max(0, prev - 10));
    }, 1000);

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
          animate={{ scale: [1, 1.1, 1], rotate: [0, 360] }}
          transition={{ 
            scale: { repeat: Infinity, duration: 3, ease: "easeInOut" },
            rotate: { repeat: Infinity, duration: 20, ease: "linear" }
          }}
          className="absolute inset-0 -m-16 bg-green-500 rounded-full opacity-30 border-4 border-green-400"
        />
        
        {/* 위치 아이콘 */}
        <div className="relative bg-white rounded-full p-12 border-8 border-green-400">
          <MapPin className="w-24 h-24 text-black" strokeWidth={3} />
        </div>

        {/* 네비게이션 화살표 */}
        <motion.div
          animate={{ y: [0, -10, 0] }}
          transition={{ repeat: Infinity, duration: 1.5, ease: "easeInOut" }}
          className="absolute -top-8 left-1/2 -translate-x-1/2"
        >
          <Navigation className="w-12 h-12 text-green-400 fill-green-400" strokeWidth={3} />
        </motion.div>
      </motion.div>

      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.3 }}
        className="mt-16 text-center"
      >
        <div className="text-yellow-400 text-lg font-bold mb-2">목적지</div>
        <h1 className="text-5xl font-bold text-white mb-8">
          {destination}
        </h1>
        
        <div className="bg-black rounded-3xl p-8 border-4 border-green-400 max-w-md">
          <div className="text-yellow-400 text-lg font-bold mb-3">남은 거리</div>
          <motion.div
            key={distance}
            initial={{ scale: 1.1 }}
            animate={{ scale: 1 }}
            className="text-7xl font-bold text-green-400 mb-2"
          >
            {distance}
          </motion.div>
          <div className="text-white text-2xl font-bold">미터</div>
        </div>
      </motion.div>

      {/* 안내 정보 */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.6 }}
        className="mt-12 bg-black rounded-2xl p-6 max-w-md border-4 border-blue-400"
      >
        <p className="text-white text-lg font-bold text-center leading-relaxed">
          목적지까지 안내 중입니다
          <br />
          <span className="text-yellow-400">
            음성 안내를 따라 이동하세요
          </span>
        </p>
      </motion.div>
    </div>
  );
}