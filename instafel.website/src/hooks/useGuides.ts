import { useState, useEffect } from "react";

interface Guide {
  id: number;
  title: string;
  subtitle: string;
  description: string;
  color: string;
  slug: string;
  content?: string;
}

export function useGuides() {
  const [guides, setGuides] = useState<Guide[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchGuides() {
      try {
        const response = await fetch("/api/guide");

        if (!response.ok) {
          throw new Error("Failed to fetch guides");
        }

        const data = await response.json();
        setGuides(data);
      } catch (err) {
        setError(
          err instanceof Error ? err.message : "An unknown error occurred"
        );
      } finally {
        setLoading(false);
      }
    }

    fetchGuides();
  }, []);

  return { guides, loading, error };
}

export function useBlogPost(id: string) {
  const [guide, setGuide] = useState<Guide | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchGuide() {
      if (!id) return;

      try {
        const response = await fetch(`/api/guide?id=${id}`);

        if (!response.ok) {
          throw new Error("Failed to fetch guide");
        }

        const data = await response.json();

        if (data.error) {
          throw new Error(data.error);
        }

        setGuide(data);
      } catch (err) {
        setError(
          err instanceof Error ? err.message : "An unknown error occurred"
        );
      } finally {
        setLoading(false);
      }
    }

    fetchGuide();
  }, [id]);

  return { guide, loading, error };
}
