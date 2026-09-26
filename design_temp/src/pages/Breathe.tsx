import React from 'react';
import { AnimatePresence, MotionConfig, motion } from 'framer-motion';
import { BreathCircle } from '../components/BreathCircle';
import { useBreathing } from '../hooks/useBreathing';

const statusText = {
  idle: 'Ты пока не дышишь',
  inhale: 'Вдох...',
  exhale: 'Выдох...'
} as const;

export function Breathe() {
  const { state, breaths, press } = useBreathing();

  return (
    <MotionConfig reducedMotion="user">
      <main className="flex min-h-full w-full flex-col items-center justify-center bg-mist px-6 py-12 text-ink">
        <div className="flex h-16 items-center sm:h-20">
          <AnimatePresence mode="wait" initial={false}>
            <motion.h1
              key={state}
              initial={{ opacity: 0, y: 8 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -8 }}
              transition={{ duration: 0.25, ease: [0.23, 1, 0.32, 1] }}
              className="text-center font-serif text-3xl leading-tight sm:text-4xl lg:text-5xl"
              aria-live="polite">
              
              {statusText[state]}
            </motion.h1>
          </AnimatePresence>
        </div>

        <div className="my-8 sm:my-12">
          <BreathCircle state={state} onPress={press} />
        </div>

        <p className="text-lg text-muted">
          Сделано вдохов: <span className="font-semibold tabular-nums text-ink">{breaths}</span>
        </p>
      </main>
    </MotionConfig>);

}