import { useTheme } from 'next-themes'
import Image from 'next/image'
import { useEffect, useState } from 'react'

export default function HomeMockup() {

    const { theme, resolvedTheme } = useTheme()
    const [mounted, setMounted] = useState(false)

    useEffect(() => setMounted(true), [])

    if (!mounted) return null

    const currentTheme = theme === 'system' ? resolvedTheme : theme
    const imageSrc = currentTheme === 'dark' ? '/mockup_dark.png' : '/mockup_light.png'

    return (
        <Image
            src={imageSrc}
            alt="Instafel App Mockup"
            width={800}
            height={800}
            quality="100"
            className="w-full h-auto object-cover rounded-xl transition-transform duration-700"
        />
    )
}
