import Link from 'next/link';

export default function NotFoundPage() {
  return (
    <div className="flex items-center justify-center min-h-screen bg-[var(--its-light-gray)]">
      <div className="text-center">
        <h1 className="text-6xl font-bold text-[var(--its-blue)] mb-4">
          404
        </h1>
        <p className="text-2xl text-[var(--its-green)] mb-6">
          Oops! The page you're looking for doesn't exist.
        </p>
        <Link href="/" passHref>
          <button className="px-8 py-3 text-white bg-[var(--its-blue)] rounded-full text-lg hover:bg-[var(--its-yellow)] transition-colors">
            Go back to Home
          </button>
        </Link>
      </div>
    </div>
  );
}
