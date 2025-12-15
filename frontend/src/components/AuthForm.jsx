import React, { useState } from 'react';
import './AuthForm.css';

// Thêm prop onLoginSuccess vào component
const AuthForm = ({ onLoginSuccess }) => {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: ''
  });

  // MẬT KHẨU MẶC ĐỊNH
  const DEFAULT_PASSWORD = "123456";

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (isLogin) {
      // --- LOGIC KIỂM TRA MẬT KHẨU MẶC ĐỊNH ---
      if (formData.password === DEFAULT_PASSWORD) {
        alert("Đăng nhập thành công!");
        // Gọi hàm của cha (App.jsx) để chuyển màn hình
        onLoginSuccess({
          email: formData.email,
          name: formData.email.split('@')[0] // Lấy tạm tên từ email
        });
      } else {
        alert(`Mật khẩu sai! Hãy dùng mật khẩu mặc định: ${DEFAULT_PASSWORD}`);
      }
    } else {
      // Logic đăng ký (Giữ nguyên hoặc xử lý tùy ý)
      if (formData.password !== formData.confirmPassword) {
        alert("Mật khẩu xác nhận không khớp!");
        return;
      }
      alert("Đăng ký thành công! Hãy đăng nhập ngay.");
      setIsLogin(true); // Chuyển về tab đăng nhập
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-form-card">
        <h2>{isLogin ? 'Đăng Nhập' : 'Tạo Tài Khoản'}</h2>
        <p className="subtitle">
          {isLogin ? 'Mật khẩu mặc định là: 123456' : 'Tham gia cùng chúng tôi ngay hôm nay.'}
        </p>

        <form onSubmit={handleSubmit}>
          {!isLogin && (
            <div className="form-group">
              <label>Tên hiển thị</label>
              <input type="text" name="username" placeholder="VD: Nguyen Van A" value={formData.username} onChange={handleChange} required={!isLogin} />
            </div>
          )}

          <div className="form-group">
            <label>Email</label>
            <input type="email" name="email" placeholder="admin@clibo.com" value={formData.email} onChange={handleChange} required />
          </div>

          <div className="form-group">
            <label>Mật khẩu</label>
            <input type="password" name="password" placeholder="••••••••" value={formData.password} onChange={handleChange} required />
          </div>

          {!isLogin && (
            <div className="form-group">
              <label>Xác nhận mật khẩu</label>
              <input type="password" name="confirmPassword" placeholder="••••••••" value={formData.confirmPassword} onChange={handleChange} required={!isLogin} />
            </div>
          )}

          <button type="submit" className="submit-btn">
            {isLogin ? 'Đăng Nhập' : 'Đăng Ký'}
          </button>
        </form>

        <div className="toggle-auth">
          <p>
            {isLogin ? 'Chưa có tài khoản? ' : 'Đã có tài khoản? '}
            <span onClick={() => setIsLogin(!isLogin)}>
              {isLogin ? 'Đăng ký ngay' : 'Đăng nhập ngay'}
            </span>
          </p>
        </div>
      </div>
    </div>
  );
};

export default AuthForm;