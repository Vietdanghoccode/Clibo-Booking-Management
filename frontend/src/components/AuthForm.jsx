import React, { useState } from 'react';
import './AuthForm.css';

// Component nhận prop onLoginSuccess từ App.jsx
const AuthForm = ({ onLoginSuccess }) => {
  const [isLogin, setIsLogin] = useState(true);

  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: ''
  });

  // MẬT KHẨU MẶC ĐỊNH CHO TẤT CẢ TÀI KHOẢN
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
      // 1. KIỂM TRA MẬT KHẨU
      if (formData.password !== DEFAULT_PASSWORD) {
        alert(`Mật khẩu sai! Hãy dùng mật khẩu mặc định: ${DEFAULT_PASSWORD}`);
        return;
      }

      // 2. XÁC ĐỊNH ROLE VÀ TÊN DỰA TRÊN EMAIL
      let role = 'PATIENT';
      let displayName = formData.username || formData.email.split('@')[0];

      // Logic phân quyền cụ thể cho kịch bản Doctor A và B
      if (formData.email === 'doctor.a@gmail.com') {
        role = 'DOCTOR';
        displayName = 'BS. Lâm Sàng (Doctor A)';
      } else if (formData.email === 'doctor.b@gmail.com') {
        role = 'DOCTOR';
        displayName = 'BS. Xét Nghiệm (Doctor B)';
      } else {
        // Mọi email khác đều là Bệnh Nhân
        role = 'PATIENT';
        if (!formData.username) {
           displayName = 'Bệnh Nhân';
        }
      }

      // 3. THÔNG BÁO VÀ GỬI DỮ LIỆU VỀ APP.JSX
      alert(`Đăng nhập thành công!\nXin chào: ${displayName}`);

      // Gửi object user về App để lưu state
      onLoginSuccess({
        email: formData.email,
        name: displayName,
        role: role
      });

    } else {
      // Logic đăng ký (Chỉ dành cho bệnh nhân mới)
      if (formData.password !== formData.confirmPassword) {
        alert("Mật khẩu xác nhận không khớp!");
        return;
      }
      console.log('Đăng ký:', formData);
      alert("Đăng ký thành công! Hãy đăng nhập ngay.");
      setIsLogin(true); // Chuyển về tab đăng nhập
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-form-card">
        <h2>{isLogin ? 'Đăng Nhập' : 'Tạo Tài Khoản'}</h2>
        <p className="subtitle">
          {isLogin ? 'Mật khẩu mặc định: 123456' : 'Tham gia cùng chúng tôi ngay hôm nay.'}
        </p>

        <form onSubmit={handleSubmit}>
          {/* Tên hiển thị chỉ hiện khi Đăng ký */}
          {!isLogin && (
            <div className="form-group">
              <label>Tên hiển thị</label>
              <input
                type="text"
                name="username"
                placeholder="VD: Nguyen Van A"
                value={formData.username}
                onChange={handleChange}
                required={!isLogin}
              />
            </div>
          )}

          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              placeholder="doctor.a@gmail.com, doctor.b@gmail.com..."
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Mật khẩu</label>
            <input
              type="password"
              name="password"
              placeholder="••••••••"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          {!isLogin && (
            <div className="form-group">
              <label>Xác nhận mật khẩu</label>
              <input
                type="password"
                name="confirmPassword"
                placeholder="••••••••"
                value={formData.confirmPassword}
                onChange={handleChange}
                required={!isLogin}
              />
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