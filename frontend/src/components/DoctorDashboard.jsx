import React, { useState } from 'react';
import './DoctorDashboard.css';

const DoctorDashboard = ({ user, appointments, onUpdateAppointment, onLogout }) => {

  // Xác định xem đây là Bác sĩ nào
  const isDoctorA = user.email === 'doctor.a@gmail.com'; // Bác sĩ khám chính
  const isDoctorB = user.email === 'doctor.b@gmail.com'; // Bác sĩ xét nghiệm

  // Dữ liệu mẫu các loại xét nghiệm
  const AVAILABLE_TESTS = [
    { id: 't1', name: 'Công thức máu' },
    { id: 't2', name: 'X-Quang Phổi' },
    { id: 't3', name: 'Siêu âm tim' }
  ];

  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [selectedTests, setSelectedTests] = useState([]);
  const [testInputs, setTestInputs] = useState({});
  const [finalDiagnosisInput, setFinalDiagnosisInput] = useState('');

  // --- LOGIC CỦA BÁC SĨ A (CHỈ ĐỊNH) ---
  const handleDoctorA_Prescribe = () => {
    if (selectedTests.length === 0) {
      alert("Vui lòng chọn ít nhất 1 xét nghiệm!"); return;
    }
    const testsToAssign = AVAILABLE_TESTS
      .filter(t => selectedTests.includes(t.id))
      .map(t => ({ ...t, result: '', status: 'PENDING' }));

    const updatedApp = {
      ...selectedAppointment,
      status: 'TESTING', // Chuyển sang trạng thái chờ Bác sĩ B
      tests: testsToAssign
    };

    onUpdateAppointment(updatedApp);
    setSelectedAppointment(null);
    alert("Đã gửi yêu cầu xét nghiệm sang cho Bác sĩ B!");
  };

  const handleDoctorA_Finalize = () => {
    if (!finalDiagnosisInput) return;
    const updatedApp = {
      ...selectedAppointment,
      diagnosis: finalDiagnosisInput,
      status: 'COMPLETED'
    };
    onUpdateAppointment(updatedApp);
    setSelectedAppointment(null);
    alert("Đã hoàn tất bệnh án!");
  };

  // --- LOGIC CỦA BÁC SĨ B (TRẢ KẾT QUẢ) ---
  const handleDoctorB_SubmitResult = (testId) => {
    const resultValue = testInputs[testId];
    if (!resultValue) return;

    const updatedTests = selectedAppointment.tests.map(t =>
      t.id === testId ? { ...t, result: resultValue, status: 'DONE' } : t
    );

    // Kiểm tra xem tất cả test đã xong chưa
    const allDone = updatedTests.every(t => t.status === 'DONE');

    // Nếu xong hết -> chuyển về cho Doctor A (DIAGNOSING). Nếu chưa -> vẫn TESTING
    const newStatus = allDone ? 'DIAGNOSING' : 'TESTING';

    const updatedApp = {
      ...selectedAppointment,
      tests: updatedTests,
      status: newStatus
    };

    // Cập nhật local state để hiển thị ngay trên UI
    setSelectedAppointment(updatedApp);
    // Cập nhật lên App cha
    onUpdateAppointment(updatedApp);
  };

  // --- RENDER CHI TIẾT ---
  if (selectedAppointment) {
    return (
      <div className="doctor-container">
        <header className="doctor-header">
           <button onClick={() => setSelectedAppointment(null)} className="back-btn">← Quay lại</button>
           <h2>Hồ sơ: {selectedAppointment.patient}</h2>
        </header>

        <div className="exam-content">
          <div className="patient-info-card">
            <p><strong>Lý do khám:</strong> {selectedAppointment.reason}</p>
            <span className={`status-badge status-${selectedAppointment.status}`}>
              {selectedAppointment.status}
            </span>
          </div>

          {/* KHU VỰC CỦA BÁC SĨ A: CHỈ ĐỊNH XÉT NGHIỆM */}
          {isDoctorA && selectedAppointment.status === 'PENDING' && (
            <div className="section-card">
              <h3>🩺 Chỉ định xét nghiệm (Dành cho BS. A)</h3>
              <div className="test-selection-list">
                {AVAILABLE_TESTS.map(test => (
                  <label key={test.id} className="test-checkbox">
                    <input type="checkbox" onChange={() => {
                        setSelectedTests(prev => prev.includes(test.id) ? prev.filter(i=>i!==test.id) : [...prev, test.id])
                    }}/> {test.name}
                  </label>
                ))}
              </div>
              <button className="primary-btn" onClick={handleDoctorA_Prescribe}>Chuyển sang phòng xét nghiệm</button>
            </div>
          )}

          {/* KHU VỰC CỦA BÁC SĨ A: XEM KẾT QUẢ & CHẨN ĐOÁN (Khi B đã làm xong) */}
          {isDoctorA && (selectedAppointment.status === 'DIAGNOSING' || selectedAppointment.status === 'TESTING') && (
            <div className="section-card">
              <h3>📊 Kết quả từ phòng xét nghiệm</h3>
              {selectedAppointment.status === 'TESTING' && <p style={{color:'orange'}}>⏳ Đang chờ Bác sĩ B nhập liệu...</p>}

              <table className="results-table">
                <thead><tr><th>Xét nghiệm</th><th>Kết quả</th><th>Trạng thái</th></tr></thead>
                <tbody>
                  {selectedAppointment.tests.map(t => (
                    <tr key={t.id}>
                      <td>{t.name}</td>
                      <td style={{fontWeight:'bold'}}>{t.result || '---'}</td>
                      <td>{t.status === 'DONE' ? '✅ Đã có KQ' : '⏳ Đang làm'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>

              {selectedAppointment.status === 'DIAGNOSING' && (
                <div style={{marginTop: '20px'}}>
                  <h3>✍️ Kết luận cuối cùng</h3>
                  <textarea className="diagnosis-area" rows="3" placeholder="Nhập kết luận..."
                    onChange={(e)=>setFinalDiagnosisInput(e.target.value)}></textarea>
                  <button className="finish-btn" onClick={handleDoctorA_Finalize}>Hoàn tất & Kê đơn</button>
                </div>
              )}
            </div>
          )}

          {/* KHU VỰC CỦA BÁC SĨ B: NHẬP KẾT QUẢ */}
          {isDoctorB && selectedAppointment.status === 'TESTING' && (
             <div className="section-card">
               <h3>🧪 Nhập kết quả xét nghiệm (Dành cho BS. B)</h3>
               <table className="results-table">
                 <thead><tr><th>Xét nghiệm</th><th>Nhập Kết quả</th><th>Hành động</th></tr></thead>
                 <tbody>
                   {selectedAppointment.tests.map(t => (
                     <tr key={t.id}>
                       <td>{t.name}</td>
                       <td>
                         {t.status === 'DONE' ? <span>{t.result}</span> : (
                           <input type="text" className="result-input" placeholder="Nhập chỉ số..."
                             onChange={(e) => setTestInputs({...testInputs, [t.id]: e.target.value})} />
                         )}
                       </td>
                       <td>
                         {t.status !== 'DONE' && (
                           <button className="save-small-btn" onClick={() => handleDoctorB_SubmitResult(t.id)}>Gửi KQ</button>
                         )}
                       </td>
                     </tr>
                   ))}
                 </tbody>
               </table>
               {selectedAppointment.tests.every(t => t.status === 'DONE') && <p style={{color:'green', marginTop:'10px'}}>✅ Đã gửi hết kết quả. Hồ sơ đã chuyển về Doctor A.</p>}
             </div>
          )}

        </div>
      </div>
    );
  }

  // --- RENDER DANH SÁCH (DASHBOARD) ---

  // Lọc danh sách bệnh nhân hiển thị tùy theo vai trò
  const filteredAppointments = appointments.filter(app => {
    if (app.status === 'COMPLETED') return true; // Cả 2 đều xem được lịch sử
    if (isDoctorA) return true; // A xem được hết để theo dõi
    if (isDoctorB) return app.status === 'TESTING'; // B chỉ thấy những người CẦN làm xét nghiệm
    return false;
  });

  return (
    <div className="doctor-container">
      <header className="doctor-header">
        <h2>{user.name}</h2>
        <button onClick={onLogout} className="logout-btn">Đăng xuất</button>
      </header>

      <div className="dashboard-content">
        <h3>{isDoctorB ? 'Danh sách cần xét nghiệm' : 'Danh sách bệnh nhân'}</h3>
        <table className="appointments-table">
          <thead>
            <tr><th>Thời gian</th><th>Bệnh nhân</th><th>Trạng thái</th><th>Hành động</th></tr>
          </thead>
          <tbody>
            {filteredAppointments.length === 0 ? (
                <tr><td colSpan="4" style={{textAlign:'center'}}>Không có bệnh nhân nào cần xử lý.</td></tr>
            ) : (
                filteredAppointments.map(app => (
                <tr key={app.id}>
                    <td>{app.time}</td>
                    <td>{app.patient}</td>
                    <td><span className={`status-badge status-${app.status}`}>{app.status}</span></td>
                    <td>
                    <button className="action-btn" onClick={() => setSelectedAppointment(app)}>
                        {isDoctorA && app.status === 'PENDING' ? 'Bắt đầu khám' :
                         isDoctorB && app.status === 'TESTING' ? 'Tiến hành XN' :
                         isDoctorA && app.status === 'DIAGNOSING' ? 'Chẩn đoán' : 'Xem hồ sơ'}
                    </button>
                    </td>
                </tr>
                ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default DoctorDashboard;