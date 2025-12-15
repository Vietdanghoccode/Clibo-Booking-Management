import React, { useState } from 'react';
import AuthForm from './components/AuthForm';
import BookingForm from './components/BookingForm';

function App() {
  // State lưu thông tin người dùng. Nếu null nghĩa là chưa đăng nhập.
  const [user, setUser] = useState(null);

  // Hàm này sẽ được truyền xuống AuthForm để gọi khi đăng nhập thành công
  const handleLoginSuccess = (userData) => {
    setUser(userData);
  };

  // Hàm đăng xuất (để test quay lại màn hình login)
  const handleLogout = () => {
    setUser(null);
  };

  return (
    <div>
      {/* Điều kiện: Có user thì hiện Booking, chưa có thì hiện Login */}
      {user ? (
        <BookingForm user={user} onLogout={handleLogout} />
      ) : (
        <AuthForm onLoginSuccess={handleLoginSuccess} />
      )}
    </div>
  );
}

export default App;