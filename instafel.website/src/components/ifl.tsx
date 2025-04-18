export function LoadingBar() {
  return (
    <div className="min-h-screen py-12 px-4 sm:px-6 lg:px-8">
      <div className="fixed inset-0 z-50 flex items-center justify-center h-full">
        <div className="relative h-16 w-16 animate-spin rounded-full border-4 border-primary border-t-transparent" />
      </div>
    </div>
  );
}

export function LoadingBarNotCenter() {
  return (
    <div className="py-12 px-4 sm:px-6 lg:px-8">
      <div className="fixed flex items-center justify-center">
        <div className="relative h-16 w-16 animate-spin rounded-full border-4 border-primary border-t-transparent" />
      </div>
    </div>
  );
}
