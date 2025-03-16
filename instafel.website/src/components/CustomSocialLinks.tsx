import Link from "next/link";
import { SocialIcons } from "@/components/Icons";
import { SITE_CONFIG } from "@/config/config";
import { cn } from "@/lib/utils";
import { SocialIconType } from "@/components/Icons";

interface CustomSocialLinksProps {
  platforms: Array<SocialIconType | "all">;
  className?: string;
  iconClassName?: string;
  direction?: "row" | "column";
}

export default function CustomSocialLinks({
  platforms,
  className,
  iconClassName,
  direction = "row",
}: CustomSocialLinksProps) {
  const filteredSocial = platforms.includes("all")
    ? SITE_CONFIG.social
    : SITE_CONFIG.social.filter((item) =>
        platforms.includes(item.platform as SocialIconType),
      );

  return (
    <div
      className={cn(
        "flex items-center gap-4",
        direction === "column" && "flex-col",
        className,
      )}
    >
      {filteredSocial.map((item, index) => {
        const Icon = SocialIcons[item.platform as SocialIconType];
        const href =
          item.platform === "Mail"
            ? `mailto:${item.username}`
            : `${item.baseUrl}${item.username}`;

        if (!Icon) return null;

        return (
          <Link
            key={index}
            href={href}
            target="_blank"
            rel="noopener noreferrer"
            className=" transition-all duration-200 hover:scale-110"
          >
            <Icon
              className={cn(
                "h-6 w-6",
                "transform transition-transform",
                iconClassName,
              )}
            />
          </Link>
        );
      })}
    </div>
  );
}
