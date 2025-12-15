import React, { useState } from 'react';
import AuthForm from './components/AuthForm';
import BookingForm from './components/BookingForm';
import DoctorDashboard from './components/DoctorDashboard';

function App() {
  const [user, setUser] = useState(null);

  // DỮ LIỆU CHUNG (DATABASE GIẢ LẬP)
  // Được lưu ở đây để chia sẻ giữa Doctor A và Doctor B
  const [appointments, setAppointments] = useState([
    {
      id: 1,
      patient: 'Nguyễn Văn A',
      age: 30, gender: 'Nam',
      time: '08:00', date: '2025-10-20',
      reason: 'Đau đầu, chóng mặt',
      status: 'PENDING', // PENDING -> TESTING -> DIAGNOSING -> COMPLETED
      tests: [],
      diagnosis: ''
    },
    {
      id: 2,
      patient: 'Trần Thị B',
      age: 45, gender: 'Nữ',
      time: '09:30', date: '2025-10-20',
      reason: 'Đau ngực trái',
      status: 'PENDING',
      tests: [],
      diagnosis: ''
    },
  ]);

  // Hàm cập nhật cuộc hẹn (Được truyền xuống DoctorDashboard)
  const handleUpdateAppointment = (updatedApp) => {
    const newList = appointments.map(app =>
      app.id === updatedApp.id ? updatedApp : app
    );
    setAppointments(newList);
  };

  const handleLoginSuccess = (userData) => {
    setUser(userData);
  };

  const handleLogout = () => {
    setUser(null);
  };

  const renderContent = () => {
    if (!user) {
      return <AuthForm onLoginSuccess={handleLoginSuccess} />;
    }

    if (user.role === 'DOCTOR') {
      return (
        <DoctorDashboard
          user={user}
          appointments={appointments}
          onUpdateAppointment={handleUpdateAppointment}
          onLogout={handleLogout}
        />
      );
    }

    return <BookingForm user={user} onLogout={handleLogout} />;
  };

  return <div>{renderContent()}</div>;
}

export default App;